package com.ighorosipov.data.datasource.postgresdatasource

import com.ighorosipov.data.datasource.GroupDataSource
import com.ighorosipov.data.model.Group
import com.ighorosipov.data.model.table.GroupTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresGroupDataSource(
    private val groupTable: GroupTable
) : GroupDataSource {
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
                    .map {}
            }
        }
    }

    override suspend fun getGroupByName(groupName: String): Group? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGroup(group: Group) {
        TODO("Not yet implemented")
    }

}