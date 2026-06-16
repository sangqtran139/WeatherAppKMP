package com.sangtq.weatherappkmp.data.storage

import com.russhwolf.settings.Settings
import com.sangtq.weatherappkmp.domain.model.FavoriteCity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

private const val KEY_FAVORITES = "favorites_v1"
private const val KEY_RECENT = "recent_searches_v1"
private const val MAX_RECENT = 8

class PreferencesStorage(private val settings: Settings) {

    private val json = Json { ignoreUnknownKeys = true }
    private val favoriteListSerializer = ListSerializer(FavoriteCity.serializer())
    private val stringListSerializer = ListSerializer(String.serializer())

    private val _favorites = MutableStateFlow(loadFavorites())
    val favorites: StateFlow<List<FavoriteCity>> = _favorites.asStateFlow()

    private val _recent = MutableStateFlow(loadRecent())
    val recent: StateFlow<List<String>> = _recent.asStateFlow()

    fun isFavorite(query: String): Boolean = _favorites.value.any { it.query.equals(query, ignoreCase = true) }

    fun toggleFavorite(city: FavoriteCity) {
        val current = _favorites.value
        val updated = if (isFavorite(city.query)) {
            current.filterNot { it.query.equals(city.query, ignoreCase = true) }
        } else {
            current + city
        }
        _favorites.value = updated
        settings.putString(KEY_FAVORITES, json.encodeToString(favoriteListSerializer, updated))
    }

    fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        val list = (listOf(query) + _recent.value.filterNot { it.equals(query, ignoreCase = true) })
            .take(MAX_RECENT)
        _recent.value = list
        settings.putString(KEY_RECENT, json.encodeToString(stringListSerializer, list))
    }

    fun clearRecent() {
        _recent.value = emptyList()
        settings.remove(KEY_RECENT)
    }

    private fun loadFavorites(): List<FavoriteCity> {
        val raw = settings.getStringOrNull(KEY_FAVORITES) ?: return emptyList()
        return runCatching { json.decodeFromString(favoriteListSerializer, raw) }.getOrDefault(emptyList())
    }

    private fun loadRecent(): List<String> {
        val raw = settings.getStringOrNull(KEY_RECENT) ?: return emptyList()
        return runCatching { json.decodeFromString(stringListSerializer, raw) }.getOrDefault(emptyList())
    }
}
