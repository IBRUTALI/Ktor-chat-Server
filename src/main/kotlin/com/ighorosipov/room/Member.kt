package com.ighorosipov.room

import io.ktor.websocket.*

data class Member(
    val userlogin: String,
    val sessionId: String,
    val socket: WebSocketSession
)