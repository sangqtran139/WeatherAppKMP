package com.sangtq.weatherappkmp.platform

import com.sangtq.weatherappkmp.domain.model.LocationResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume

actual class LocationProvider {

    actual suspend fun getCurrentLocation(): LocationResult {
        val status = CLLocationManager.authorizationStatus()
        if (status == kCLAuthorizationStatusDenied || status == kCLAuthorizationStatusRestricted) {
            return LocationResult.PermissionDenied
        }

        return suspendCancellableCoroutine { cont ->
            val delegate = LocationDelegate { result ->
                if (cont.isActive) cont.resume(result)
            }
            delegate.manager.delegate = delegate

            if (status == kCLAuthorizationStatusNotDetermined) {
                delegate.manager.requestWhenInUseAuthorization()
            } else {
                delegate.manager.requestLocation()
            }

            cont.invokeOnCancellation { delegate.manager.stopUpdatingLocation() }
        }
    }
}

private class LocationDelegate(
    private val onResult: (LocationResult) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {

    val manager = CLLocationManager()

    @OptIn(ExperimentalForeignApi::class)
    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val location = didUpdateLocations.filterIsInstance<CLLocation>().lastOrNull() ?: return
        val lat = location.coordinate.useContents { latitude }
        val lon = location.coordinate.useContents { longitude }
        onResult(LocationResult.Success(lat, lon))
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        onResult(LocationResult.Error(didFailWithError.localizedDescription))
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        val status: CLAuthorizationStatus = manager.authorizationStatus
        when (status) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> manager.requestLocation()
            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> onResult(LocationResult.PermissionDenied)
            else -> {}
        }
    }
}
