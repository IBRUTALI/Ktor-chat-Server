package com.ighorosipov.data.datasource.postgresdatasource

import com.ighorosipov.data.datasource.MessageDataSource
import com.ighorosipov.data.model.Message
import com.ighorosipov.data.model.table.GroupTable.uuid
import com.ighorosipov.data.model.table.MessageTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresMessageDataSource(
    private val messageTable: MessageTable
) : MessageDataSource {

    override suspend fun getAllMessages(groupId: String): List<Message> {
        return withContext(Dispatchers.IO) {
            transaction {
                messageTable.select { MessageTable.groupId.eq(uuid(groupId)) }
                    .mapNotNull {
                        rowToMessage(it)
                    }
            }
        }
    }

    override suspend fun insertMessage(message: Message) {
        return withContext(Dispatchers.IO) {
            transaction {
                messageTable.insert { table ->
                    table[id] = uuid(message.id)
                    table[groupId] = uuid(message.groupId)
                    table[text] = message.text
                    table[userlogin] = message.userlogin
                    table[timestamp] = message.timestamp
                }
            }
        }
    }

    override suspend fun getLastMessage(): Message? {
        TODO()
    }

    override suspend fun deleteMessages(selectedMessages: List<Message>) {
        TODO()
    }

    override suspend fun updateMessage(message: Message) {
        TODO()
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