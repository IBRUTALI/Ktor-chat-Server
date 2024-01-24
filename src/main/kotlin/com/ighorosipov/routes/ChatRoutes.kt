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
        webSocket("/chat-socket") {
            val session = call.sessions.get<ChatSession>()
            val login = call.principal<JWTPrincipal>()?.getClaim("userlogin", String::class) ?: return@webSocket
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

fun Route.groupsSocket(roomController: RoomController) {
    authenticate {
        webSocket {
            val session = call.sessions.get<ChatSession>()
            val login = call.principal<JWTPrincipal>()?.getClaim("userlogin", String::class) ?: return@webSocket
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

fun Route.getAllMessages(roomController: RoomController) {
    authenticate {
        get("/messages") {
            val session = call.sessions.get<ChatSession>()
            if (session != null) {
                call.respond(
                    HttpStatusCode.OK,
                    roomController.getAllMessages(session.groupId)
                )
            }
        }
    }
}