package com.ighorosipov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val userlogin: String,
    val username: String,
    val avatarUrl: String ?= null
)
