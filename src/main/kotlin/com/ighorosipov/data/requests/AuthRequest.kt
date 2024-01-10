package com.ighorosipov.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val userlogin: String,
    val username: String,
    val password: String
)
