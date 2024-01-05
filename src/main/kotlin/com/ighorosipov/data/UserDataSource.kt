package com.ighorosipov.data

import com.ighorosipov.data.model.User

interface UserDataSource {

    suspend fun getUserByLogin(login: String): User?

    suspend fun insertNewUser(user: User): Boolean

}