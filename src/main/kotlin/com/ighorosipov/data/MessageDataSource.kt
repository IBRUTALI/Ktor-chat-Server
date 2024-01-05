package com.ighorosipov.data

import com.ighorosipov.data.model.Message

interface MessageDataSource {

    suspend fun getAllMessages(): List<Message>

    suspend fun insertMessage(message: Message)

    suspend fun getLastMessage(): Message?

    suspend fun deleteMessages(messages: List<Message>)

    suspend fun updateMessage(message: Message)

}