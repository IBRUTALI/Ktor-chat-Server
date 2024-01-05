package com.ighorosipov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val text: String,
    val username: String,
    val timestamp: Long,
)
