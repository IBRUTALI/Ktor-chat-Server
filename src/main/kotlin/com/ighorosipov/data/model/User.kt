package com.ighorosipov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userlogin: String,
    val username: String,
    val avatarUrl: String? = null,
    val password: String,
    val salt: String
)
