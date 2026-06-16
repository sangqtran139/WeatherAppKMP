package com.sangtq.weatherappkmp.domain.model

data class SportsData(
    val football: List<SportEvent>,
    val cricket: List<SportEvent>,
    val golf: List<SportEvent>
)

data class SportEvent(
    val stadium: String,
    val country: String,
    val region: String,
    val tournament: String,
    val start: String,
    val match: String
)

enum class SportCategory(val display: String) {
    FOOTBALL("Football"),
    CRICKET("Cricket"),
    GOLF("Golf")
}
