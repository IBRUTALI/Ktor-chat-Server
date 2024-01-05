package com.ighorosipov.data.model

import kotlinx.serialization.Serializable
@Serializable
data class GroupChat(
    val name: String,
    val membersCount: String,
)
