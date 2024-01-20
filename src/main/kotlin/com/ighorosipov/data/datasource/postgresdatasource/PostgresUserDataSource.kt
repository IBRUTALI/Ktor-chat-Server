package com.ighorosipov.data.datasource.postgresdatasource

import com.ighorosipov.data.datasource.UserDataSource
import com.ighorosipov.data.model.User
import com.ighorosipov.data.model.UserProfile
import com.ighorosipov.data.model.table.UserTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class PostgresUserDataSource : UserDataSource {

    private val userTable = UserTable

    override suspend fun getUserByLogin(login: String) = withContext(Dispatchers.IO) {
        transaction {
            userTable.selectAll().where { UserTable.userlogin.eq(login) }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun getUserProfileByLogin(login: String) = withContext(Dispatchers.IO) {
        transaction {
            userTable.selectAll().where { UserTable.userlogin.eq(login) }
                .map { rowToUserProfile(it) }
                .singleOrNull()
        }
    }

    override suspend fun insertNewUser(user: User): Boolean {
        withContext(Dispatchers.IO) {
            transaction {
                if (userTable.selectAll().where { UserTable.userlogin.eq(user.userlogin) }.count() == 1L ) {
                    return@transaction false
                }
                userTable.insert { table ->
                    table[userlogin] = user.userlogin
                    table[username] = user.username
                    table[password] = user.password
                    table[salt] = user.salt
                }
            }
        }
        return true
    }

    override suspend fun updateUserProfile(userProfile: UserProfile) {
        return withContext(Dispatchers.IO) {
            transaction {
                userTable.update({UserTable.userlogin.eq(userProfile.userlogin)}) { table ->
                    table[userlogin] = userProfile.userlogin
                    table[username] = userProfile.username
                    table[avatarUrl] = userProfile.avatarUrl
                }
            }
        }
    }

    private fun rowToUser(row: ResultRow?): User? {
        return row?.let {
            User(
                userlogin = row[UserTable.userlogin],
                username = row[UserTable.username],
                password = row[UserTable.password],
                salt = row[UserTable.salt]
            )
        }
    }

    private fun rowToUserProfile(row: ResultRow?): UserProfile? {
        return row?.let {
            UserProfile(
                userlogin = row[UserTable.userlogin],
                username = row[UserTable.username],
                avatarUrl = row[UserTable.avatarUrl]
            )
        }
    }

}