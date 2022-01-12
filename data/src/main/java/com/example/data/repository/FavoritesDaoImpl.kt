package com.example.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.data.datasource.FavoritesDao
import com.example.presentation.model.ArtistDisplay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.flow.map

class FavoritesDaoImpl (
    private val sharedPreferences: SharedPreferences
        ) : FavoritesDao {

    companion object {

        private const val FAVORITES_KEY = "FAVORITES_KEY"
    }

    private var artists: List<ArtistDisplay>
        get() {
            return sharedPreferences.getString(FAVORITES_KEY, null)?.let {
                Json.decodeFromString(it)
            } ?: emptyList()
        }
        set(value) {
            state.value = value
            sharedPreferences.edit {
                putString(FAVORITES_KEY, Json.encodeToString(value))
            }
        }

    private val state = MutableStateFlow(artists)

    override suspend fun add(artist: ArtistDisplay) {
        artists = artists + artist
    }

    override suspend fun delete(artist: ArtistDisplay) {
        artists = artists - artist
    }

    override suspend fun isInFavorites(artist: ArtistDisplay): Boolean {
        return artists.contains(artist)
    }

    override fun getFavorites(): Flow<List<ArtistDisplay>> = state

    override fun getCount(): Flow<Int> = state.map { it.size }
}