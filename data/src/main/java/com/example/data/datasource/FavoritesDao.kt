package com.example.data.datasource

import com.example.domain.model.ArtistModel
import kotlinx.coroutines.flow.Flow

interface FavoritesDao {

    suspend fun add(artist: ArtistModel?)

    suspend fun delete(artist: ArtistModel?)

    suspend fun isInFavorites(artist: ArtistModel?): Boolean

    fun getFavorites(): Flow<List<ArtistModel>>

    fun getCount(): Flow<Int>
}