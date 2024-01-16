package com.ighorosipov.data.model

data class UserProfile(
    val userlogin: String,
    val username: String,
    val avatarUrl: String ?= null
)
