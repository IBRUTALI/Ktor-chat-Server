package com.ighorosipov.data.datasource.postgresdatasource

import com.ighorosipov.data.datasource.UserDataSource
import com.ighorosipov.data.model.User
import com.ighorosipov.data.model.table.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresUserDataSource : UserDataSource {

    private val userTable = UserTable

    override suspend fun getUserByLogin(login: String) = withContext(Dispatchers.IO) {
        transaction {
            userTable.selectAll().where { UserTable.userlogin.eq(login) }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun insertNewUser(user: User): Boolean {
        withContext(Dispatchers.IO) {
            transaction {
                userTable.insert { table ->
                    table[userlogin] = user.userlogin
                    table[username] = user.username
                    table[avatarUrl] = user.avatarUrl
                    table[password] = user.password
                    table[salt] = user.salt
                }
            }
        }
        return true
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

}