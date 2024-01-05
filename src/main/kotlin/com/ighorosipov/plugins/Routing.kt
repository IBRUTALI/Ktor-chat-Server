package com.ighorosipov.plugins

import com.ighorosipov.room.RoomController
import com.ighorosipov.routes.chatSocket
import com.ighorosipov.routes.getAllMessages
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
//    val roomController by inject<RoomController>()
    install(Routing) {
//        chatSocket(roomController)
//        getAllMessages(roomController)
    }
}
