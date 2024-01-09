package com.ighorosipov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: String,
    val name: String,
    val subscribers: List<Message>,
    val owner: String,
    val createdAt: Long
)
