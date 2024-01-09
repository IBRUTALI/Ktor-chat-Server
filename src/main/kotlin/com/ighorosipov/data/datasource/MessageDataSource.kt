package com.ighorosipov.data.datasource

import com.ighorosipov.data.model.Message

interface MessageDataSource {

    suspend fun getAllMessages(groupId: String): List<Message>

    suspend fun insertMessage(message: Message)

    suspend fun getLastMessage(): Message?

    suspend fun deleteMessages(messages: List<Message>)

    suspend fun updateMessage(message: Message)

}