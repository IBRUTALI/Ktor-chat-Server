package com.ighorosipov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val name: String,
    val owner: String,
    val createdAt: Long
)
