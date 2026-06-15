package com.sangtq.weatherappkmp.data.mapper

import com.sangtq.weatherappkmp.domain.model.SearchLocation
import com.sangtq.weatherappkmp.model.search.SearchLocationDto

fun SearchLocationDto.toDomain() = SearchLocation(
    id = id,
    name = name,
    region = region,
    country = country,
    lat = lat,
    lon = lon,
    url = url
)
