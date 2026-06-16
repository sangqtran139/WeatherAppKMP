package com.sangtq.weatherappkmp.model.ip

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IpLookupDto(
    val ip: String = "",
    val type: String = "",
    @SerialName("continent_code") val continentCode: String = "",
    @SerialName("continent_name") val continentName: String = "",
    @SerialName("country_code") val countryCode: String = "",
    @SerialName("country_name") val countryName: String = "",
    @SerialName("is_eu") val isEu: String = "",
    @SerialName("geoname_id") val geonameId: Long = 0,
    val city: String = "",
    val region: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    @SerialName("tz_id") val tzId: String = "",
    @SerialName("localtime_epoch") val localtimeEpoch: Long = 0,
    val localtime: String = ""
)
