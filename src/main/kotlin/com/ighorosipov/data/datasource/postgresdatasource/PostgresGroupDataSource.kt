package com.ighorosipov.data.datasource.postgresdatasource

import com.ighorosipov.data.datasource.GroupDataSource
import com.ighorosipov.data.model.Group
import com.ighorosipov.data.model.GroupWithMessages
import com.ighorosipov.data.model.Message
import com.ighorosipov.data.model.User
import com.ighorosipov.data.model.table.GroupTable
import com.ighorosipov.data.model.table.MessageTable
import com.ighorosipov.data.model.table.UserSubscriptionsTable
import com.ighorosipov.data.model.table.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PostgresGroupDataSource : GroupDataSource {

    private val groupTable = GroupTable
    private val messageTable = MessageTable.alias("u1")
    private val userSubscriptionsTable = UserSubscriptionsTable.alias("usersubscriptions")
    private val userTable = UserTable.alias("u3")

    override suspend fun createGroup(group: Group) {
        withContext(Dispatchers.IO) {
            transaction {
                groupTable.insert { table ->
                    table[name] = group.name
                    table[owner] = group.owner
                    table[createdAt] = group.createdAt
                }
            }
        }
    }

    override suspend fun getAllGroupsInfo(userlogin: String): List<GroupWithMessages> {
        return withContext(Dispatchers.IO) {
            transaction {
                groupTable
                    .innerJoin(
                        userSubscriptionsTable,
                        { id },
                        { userSubscriptionsTable[UserSubscriptionsTable.groupId] })
                    .selectAll()
                    .where { UserSubscriptionsTable.userlogin.eq(userlogin) }
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

    override suspend fun subscribeToGroup(userlogin: String, groupId: String) {
        withContext(Dispatchers.IO) {
            transaction {
                UserSubscriptionsTable.insert { table ->
                    table[UserSubscriptionsTable.userlogin] = userlogin
                    table[UserSubscriptionsTable.groupId] = UUID.fromString(groupId)
                    table[UserSubscriptionsTable.isSubscribed] = true

                }
            }
        }
    }

    override suspend fun unsubscribeFromGroup(userlogin: String, groupId: String) {
        withContext(Dispatchers.IO) {
            transaction {
//                userSubscriptionsTable.deleteWhere {
//                    UserSubscriptionsTable.userlogin.eq(userlogin) and UserSubscriptionsTable.groupId.eq(UUID.fromString(groupId))
//                }
                TODO()
            }
        }
    }

    private fun getSubscribersByGroupId(groupId: EntityID<UUID>): List<User> {
        return transaction {
            userTable
                .innerJoin(
                    userSubscriptionsTable,
                    { UserTable.userlogin },
                    { userSubscriptionsTable[UserSubscriptionsTable.userlogin] })
                .selectAll()
                .where { UserSubscriptionsTable.groupId.eq(groupId) }
                .mapNotNull {
                    rowToUser(it)
                }
        }
    }

    private fun getMessagesByGroupId(groupId: EntityID<UUID>): List<Message> {
        return transaction {
            messageTable
                .selectAll()
                .where { MessageTable.groupId.eq(groupId) }
                .mapNotNull {
                    rowToMessage(it)
                }
        }
    }

    private fun rowToGroup(row: ResultRow?): Group? {
        return row?.let {
            Group(
                name = row[GroupTable.name],
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
                subscribers = getSubscribersByGroupId(row[GroupTable.id]),
                owner = row[GroupTable.owner],
                messages = getMessagesByGroupId(row[GroupTable.id]),
                createdAt = row[GroupTable.createdAt]
            )
        }
    }

    private fun rowToUser(row: ResultRow?): User? {
        return row?.let {
            User(
                userlogin = row[UserTable.userlogin],
                username = row[UserTable.username],
                avatarUrl = row[UserTable.avatarUrl],
                password = row[UserTable.password],
                salt = row[UserTable.salt]
            )
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
