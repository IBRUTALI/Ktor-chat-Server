package com.ighorosipov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val groupId: String,
    val text: String,
    val userlogin: String,
    val isEdited: Boolean = false,
    val timestamp: Long,
)
