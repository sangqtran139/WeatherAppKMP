package com.sangtq.weatherappkmp.platform

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.sangtq.weatherappkmp.domain.model.LocationResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.milliseconds

actual class LocationProvider(private val context: Context) {

    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocation(): LocationResult {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return LocationResult.PermissionDenied

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val lastKnown = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

        if (lastKnown != null) {
            return LocationResult.Success(lastKnown.latitude, lastKnown.longitude)
        }

        return withTimeoutOrNull(10_000L.milliseconds) {
            suspendCancellableCoroutine { cont ->
                val listener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        locationManager.removeUpdates(this)
                        if (cont.isActive) cont.resume(LocationResult.Success(location.latitude, location.longitude))
                    }
                    override fun onProviderDisabled(provider: String) {
                        locationManager.removeUpdates(this)
                        if (cont.isActive) cont.resume(LocationResult.Error("Provider disabled"))
                    }
                }
                try {
                    val provider = when {
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                        else -> {
                            cont.resume(LocationResult.Error("No location provider available"))
                            return@suspendCancellableCoroutine
                        }
                    }
                    locationManager.requestLocationUpdates(provider, 0L, 0f, listener, Looper.getMainLooper())
                    cont.invokeOnCancellation { locationManager.removeUpdates(listener) }
                } catch (e: SecurityException) {
                    cont.resume(LocationResult.PermissionDenied)
                }
            }
        } ?: LocationResult.Error("Location timeout")
    }
}
