package com.ighorosipov.data.datasource

import com.ighorosipov.data.model.Group

interface GroupDataSource {

    suspend fun createGroup(group: Group)

    suspend fun updateGroup(group: Group)

    suspend fun getGroups(userlogin: String): List<Group>

    suspend fun getGroupByName(groupName: String): Group?

    suspend fun deleteGroup(group: Group)

}