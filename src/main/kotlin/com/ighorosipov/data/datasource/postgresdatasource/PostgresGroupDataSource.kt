package com.ighorosipov.data.datasource.postgresdatasource

import com.ighorosipov.data.datasource.GroupDataSource
import com.ighorosipov.data.model.Group
import com.ighorosipov.data.model.GroupWithMessages
import com.ighorosipov.data.model.table.GroupTable
import com.ighorosipov.data.model.table.GroupTable.innerJoin
import com.ighorosipov.data.model.table.MessageTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresGroupDataSource : GroupDataSource {

    private val groupTable = GroupTable
    private val messageTable = MessageTable.alias("u1")

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


    override suspend fun getGroups(userlogin: String): List<Group> {
        return withContext(Dispatchers.IO) {
            transaction {
                groupTable.selectAll().where { GroupTable.arrayOfSubscribers like userlogin }
                    .mapNotNull {
                        rowToGroup(it)
                    }
            }
        }
    }

    override suspend fun getAllMessagesFromGroups(userlogin: String): List<GroupWithMessages> {
        return withContext(Dispatchers.IO) {
            transaction {
                groupTable
                    .innerJoin(messageTable, {id}, {messageTable[MessageTable.groupId]})
                    .selectAll()
                    .where { GroupTable.arrayOfSubscribers.eq(userlogin) }
                    .mapNotNull {
                        rowToGroupWithMessages(it)
                    }
            }
        }
    }

    override suspend fun updateGroup(group: Group) {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupByName(groupName: String): Group? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGroup(group: Group) {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeToGroup() {
        TODO("Not yet implemented")
    }

    override suspend fun unsubscribeFromGroup() {
        TODO("Not yet implemented")
    }

    private fun rowToGroup(row: ResultRow?): Group? {
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

    private fun rowToGroupWithMessages(row: ResultRow?): GroupWithMessages? {
        return row?.let {
            GroupWithMessages(
                id = row[GroupTable.id].toString(),
                name = row[GroupTable.name],
                subscribers = row[GroupTable.arrayOfSubscribers].split(" "),
                owner = row[GroupTable.owner],
                messages = row[],
                createdAt = row[GroupTable.createdAt]
            )
        }
    }

}