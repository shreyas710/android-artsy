package com.example.assignment4.home.data.models

import com.google.gson.annotations.SerializedName

data class Artist(
    val type: String,
    val title: String,
    val description: String?,
    @SerializedName("og_type")
    val ogType: String,
    @SerializedName("_links")
    val links: Links,
    val name: String?,
    val id: String?,
)

data class Links(
    val self: Link,
    val permalink: Link,
    val thumbnail: Link
)

data class Link(
    val href: String
)