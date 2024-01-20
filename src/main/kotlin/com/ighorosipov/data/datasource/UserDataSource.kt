package com.ighorosipov.data.datasource

import com.ighorosipov.data.model.User
import com.ighorosipov.data.model.UserProfile

interface UserDataSource {

    suspend fun getUserByLogin(login: String): User?

    suspend fun getUserProfileByLogin(login: String): UserProfile?

    suspend fun insertNewUser(user: User): Boolean

    suspend fun updateUserProfile(userProfile: UserProfile)

}