package com.sangtq.weatherappkmp.data.mapper

import com.sangtq.weatherappkmp.domain.model.IpLocation
import com.sangtq.weatherappkmp.model.ip.IpLookupDto

fun IpLookupDto.toDomain() = IpLocation(
    ip = ip,
    city = city,
    region = region,
    country = countryName,
    lat = lat,
    lon = lon,
    tzId = tzId
)
