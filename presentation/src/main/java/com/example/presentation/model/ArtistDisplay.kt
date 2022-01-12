package com.example.presentation.model

import com.example.domain.model.ArtistModel
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDisplay(
    val image: String?,
    val title: String,
    val profile: String?,
    val members: String?
)

fun ArtistModel.toDisplay() = ArtistDisplay(
    images?.firstOrNull()?.resourceUrl,
    name,
    profile,
    members?.joinToString(separator = "\n") { it.name }
)