package com.ighorosipov.data.datasource.postgresdatasource

import com.ighorosipov.data.datasource.GroupDataSource
import com.ighorosipov.data.model.Group
import com.ighorosipov.data.model.Message
import com.ighorosipov.data.model.table.GroupTable
import com.ighorosipov.data.model.table.MessageTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresGroupDataSource : GroupDataSource {

    private val groupTable = GroupTable

    override suspend fun createGroup(group: Group) {
        withContext(Dispatchers.IO) {
            transaction {
                groupTable.insert { table ->
                    table[id] = uuid(group.id)
                    table[name] = group.name
                    table[arrayOfSubscribers] = group.subscribers.joinToString(" ")
                    table[owner] = group.owner
                    table[createdAt] = group.createdAt
                }
            }
        }
    }

    override suspend fun updateGroup(group: Group) {
        TODO("Not yet implemented")
    }

    override suspend fun getGroups(userlogin: String): List<Group> {
        return withContext(Dispatchers.IO) {
            transaction {
                groupTable.selectAll().where { GroupTable.arrayOfSubscribers like userlogin }
                    .mapNotNull {
                        rowToMessage(it)
                    }
            }
        }
    }

    override suspend fun getGroupByName(groupName: String): Group? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGroup(group: Group) {
        TODO("Not yet implemented")
    }

    private fun rowToMessage(row: ResultRow?): Group? {
        return row?.let {
            Group(
                id = row[GroupTable.id].toString(),
                name = row[GroupTable.name],
                subscribers = row[GroupTable.arrayOfSubscribers].split(" "),
                owner = row[GroupTable.owner],
                createdAt = row[GroupTable.createdAt]
            )
        }
    }

}