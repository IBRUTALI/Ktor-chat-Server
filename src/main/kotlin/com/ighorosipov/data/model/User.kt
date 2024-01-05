package com.ighorosipov.data.model

data class User(
    val userlogin: String,
    val username: String,
    val avatarUrl: String,
    val password: String,
    val salt: String
)
