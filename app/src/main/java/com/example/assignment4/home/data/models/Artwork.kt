package com.example.assignment4.home.data.models

data class Dimensions(
    val text: String,
    val height: Double,
    val width: Double,
    val depth: Double?,
    val diameter: Double?
)

data class DimensionSet(
    val `in`: Dimensions,
    val cm: Dimensions
)

data class ArtworkLinks(
    val thumbnail: ArtworkLink,
    val image: ImageLink,
    val partner: ArtworkLink,
    val self: ArtworkLink,
    val permalink: ArtworkLink,
    val genes: ArtworkLink,
    val artists: ArtworkLink,
    val similar_artworks: ArtworkLink,
    val collection_users: ArtworkLink,
    val sale_artworks: ArtworkLink
)

data class ArtworkLink(
    val href: String
)

data class ImageLink(
    val href: String,
    val templated: Boolean
)

data class Embedded(
    val editions: List<Any>
)

data class Artwork(
    val id: String,
    val slug: String,
    val created_at: String,
    val updated_at: String,
    val title: String,
    val category: String,
    val medium: String,
    val date: String,
    val dimensions: DimensionSet,
    val published: Boolean,
    val website: String,
    val signature: String,
    val series: String?,
    val provenance: String,
    val literature: String,
    val exhibition_history: String,
    val collecting_institution: String,
    val additional_information: String,
    val image_rights: String,
    val blurb: String,
    val unique: Boolean,
    val cultural_maker: String?,
    val iconicity: Double,
    val can_inquire: Boolean,
    val can_acquire: Boolean,
    val can_share: Boolean,
    val sale_message: String?,
    val sold: Boolean,
    val visibility_level: String,
    val image_versions: List<String>,
    val _links: ArtworkLinks,
    val _embedded: Embedded
)
