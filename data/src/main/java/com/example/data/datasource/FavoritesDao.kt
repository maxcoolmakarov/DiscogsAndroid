package com.example.data.datasource

import kotlinx.coroutines.flow.Flow
import com.example.presentation.model.ArtistDisplay

interface FavoritesDao {

    suspend fun add(artist: ArtistDisplay)

    suspend fun delete(artist: ArtistDisplay)

    suspend fun isInFavorites(artist: ArtistDisplay): Boolean

    fun getFavorites(): Flow<List<ArtistDisplay>>

    fun getCount(): Flow<Int>
}