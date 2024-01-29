package com.ighorosipov.room

import com.ighorosipov.data.datasource.GroupDataSource
import com.ighorosipov.data.datasource.MessageDataSource
import com.ighorosipov.data.model.GroupWithMessages
import com.ighorosipov.data.model.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource,
    private val groupDataSource: GroupDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        userlogin: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if(members.containsKey(userlogin)) {
            throw MemberAlreadyExistsException()
        }
        members[userlogin] = Member(
            userlogin = userlogin,
            sessionId = sessionId,
            socket = socket
        )
    }

   suspend fun sendMessage(senderLogin: String, message: String, groupId: String) {
       val messageEntity = Message(
           groupId = groupId,
           text = message,
           userlogin = senderLogin,
           timestamp = System.currentTimeMillis()
       )
       if (!groupDataSource.isUserSubscribed(senderLogin, groupId)) {
           throw Exception()
       }
       val uuidMessage = messageDataSource.insertMessage(messageEntity)

        members.values.forEach { member ->
            if (groupDataSource.isUserSubscribed(member.userlogin, groupId)) {
                val parsedMessage = Json.encodeToString(messageEntity.copy(id = uuidMessage))
                member.socket.send(Frame.Text(parsedMessage))
            }
        }
    }

    suspend fun getAllGroupsInfo(userlogin: String): List<GroupWithMessages> {
        return groupDataSource.getAllGroupsInfo(userlogin)
    }

    suspend fun tryDisconnect(userlogin: String) {
        members[userlogin]?.socket?.close()
        if (members.containsKey(userlogin)) {
            members.remove(userlogin)
        }
    }

}