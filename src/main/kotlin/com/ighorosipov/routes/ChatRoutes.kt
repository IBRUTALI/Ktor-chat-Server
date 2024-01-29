package com.ighorosipov.routes

import com.ighorosipov.room.MemberAlreadyExistsException
import com.ighorosipov.room.RoomController
import com.ighorosipov.session.ChatSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocket(roomController: RoomController) {
    authenticate {
        webSocket {
            val session = call.sessions.get<ChatSession>()
            val login = call.principal<JWTPrincipal>()?.getClaim(USERLOGIN, String::class)
            if(login == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Login is undefined."))
                return@webSocket
            }
            if (session == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
                return@webSocket
            }
            try {
                roomController.onJoin(
                    userlogin = login,
                    sessionId = session.sessionId,
                    socket = this
                )
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        roomController.sendMessage(
                            senderLogin = login,
                            groupId = session.groupId,
                            message = frame.readText()
                        )
                    }
                }
            } catch (e: MemberAlreadyExistsException) {
                call.respond(HttpStatusCode.Conflict)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                roomController.tryDisconnect(login)
            }
        }
    }
}

fun Route.getAllGroupsInfo(roomController: RoomController) {
    authenticate {
        get {
            val session = call.sessions.get<ChatSession>()
            val login = call.principal<JWTPrincipal>()?.getClaim(USERLOGIN, String::class)
            if (session != null && login != null) {
                call.respond(
                    HttpStatusCode.OK,
                    roomController.getAllGroupsInfo(login)
                )
            }
        }

    }
}