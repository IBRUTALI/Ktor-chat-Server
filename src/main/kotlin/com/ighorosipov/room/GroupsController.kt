package com.ighorosipov.room

import com.ighorosipov.data.datasource.GroupDataSource
import com.ighorosipov.data.datasource.MessageDataSource
import com.ighorosipov.data.model.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class GroupsController(
    private val messageDataSource: MessageDataSource,
    private val groupDataSource: GroupDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        userlogin: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        members[userlogin] = Member(
            userlogin = userlogin,
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun sendMessage(senderLogin: String, message: String, groupId: String) {
        members.values.forEach { member ->
            val messageEntity = Message(
                id = UUID.randomUUID().toString(),
                groupId = groupId,
                text = message,
                userlogin = senderLogin,
                timestamp = System.currentTimeMillis()
            )
            messageDataSource.insertMessage(messageEntity)

            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessagesForGroup(groupId: String): List<Message> {
        return messageDataSource.getAllMessagesForGroup(groupId)
    }

    suspend fun getAllMessagesFromGroups(userlogin: String): List<Message> {
        return messageDataSource.getAllMessagesForGroup(userlogin)
    }

    suspend fun tryDisconnect(userlogin: String) {
        members[userlogin]?.socket?.close()
        if (members.containsKey(userlogin)) {
            members.remove(userlogin)
        }
    }

}