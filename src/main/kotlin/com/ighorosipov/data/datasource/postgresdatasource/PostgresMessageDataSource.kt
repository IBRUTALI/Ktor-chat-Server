package com.ighorosipov.data.datasource.postgresdatasource

import com.ighorosipov.data.datasource.MessageDataSource
import com.ighorosipov.data.model.Message
import com.ighorosipov.data.model.table.GroupTable.uuid
import com.ighorosipov.data.model.table.MessageTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

class PostgresMessageDataSource : MessageDataSource {

    private val messageTable = MessageTable

    override suspend fun getAllMessagesForGroup(groupId: String): List<Message> {
        return withContext(Dispatchers.IO) {
            transaction {
                messageTable.selectAll()
                    .where { MessageTable.groupId.eq(uuid(groupId)) }
                    .mapNotNull {
                        rowToMessage(it)
                    }
            }
        }
    }

    override suspend fun insertMessage(message: Message): String? {
        return withContext(Dispatchers.IO) {
            transaction {
                messageTable.insert { table ->
                    table[groupId] = UUID.fromString(message.groupId)
                    table[text] = message.text
                    table[userlogin] = message.userlogin
                    table[isEdited] = message.isEdited
                    table[timestamp] = message.timestamp
                }.resultedValues?.first()?.get(MessageTable.id).toString()
            }
        }
    }

    override suspend fun getLastMessage(): Message? {
        TODO()
    }

    override suspend fun deleteMessages(messages: List<Message>) {
        TODO()
    }

    override suspend fun updateMessage(message: Message) {
        withContext(Dispatchers.IO) {
            transaction {
                messageTable.update({ MessageTable.id.eq(UUID.fromString(message.id)) }) { table ->
                        table[MessageTable.text] = message.text
                        table[MessageTable.isEdited] = true
                    }
            }
        }
    }

    private fun rowToMessage(row: ResultRow?): Message? {
        return row?.let {
            Message(
                id = row[MessageTable.id].toString(),
                groupId = row[MessageTable.groupId].toString(),
                text = row[MessageTable.text],
                userlogin = row[MessageTable.userlogin],
                timestamp = row[MessageTable.timestamp]
            )
        }
    }

}