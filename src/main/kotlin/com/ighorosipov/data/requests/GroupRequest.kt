package com.ighorosipov.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class GroupRequest(
    val name: String,
    val owner: String
)
