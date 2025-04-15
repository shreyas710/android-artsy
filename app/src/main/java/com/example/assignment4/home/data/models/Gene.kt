package com.example.assignment4.home.data.models

import com.google.gson.annotations.SerializedName

data class GeneCategory(
    @SerializedName("id")
    val id: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("image_versions")
    val imageVersions: List<String>,

    @SerializedName("_links")
    val links: GeneLinks
)

data class GeneLinks(
    @SerializedName("thumbnail")
    val thumbnail: ResourceLink,

    @SerializedName("image")
    val image: GeneImageLink,

    @SerializedName("self")
    val self: ResourceLink,

    @SerializedName("permalink")
    val permalink: ResourceLink,

    @SerializedName("artworks")
    val artworks: ResourceLink,

    @SerializedName("published_artworks")
    val publishedArtworks: ResourceLink,

    @SerializedName("artists")
    val artists: ResourceLink
)

data class ResourceLink(
    @SerializedName("href")
    val href: String
)

data class GeneImageLink(
    @SerializedName("href")
    val href: String,

    @SerializedName("templated")
    val templated: Boolean
)
