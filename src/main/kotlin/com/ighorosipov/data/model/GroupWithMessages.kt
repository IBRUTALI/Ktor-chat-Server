package com.ighorosipov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupWithMessages(
    val id: String,
    val name: String,
    val subscribers: List<UserProfile>,
    val owner: String,
    val createdAt: Long,
    val messages: List<Message>
)