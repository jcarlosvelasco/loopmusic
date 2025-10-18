package com.example.jcarlosvelasco.loopmusic.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Artists(
    val href: String,
    val items: List<Artist>,
    val limit: Int,
    val next: String? = null,
    val offset: Int,
    val previous: String? = null,
    val total: Int
)