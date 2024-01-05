package com.ighorosipov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val name: String,
    val subscribers: Long,
    val isSubscriber: Boolean,
    val messageStory: List<Message>
)
