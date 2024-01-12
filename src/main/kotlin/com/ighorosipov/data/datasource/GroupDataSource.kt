package com.ighorosipov.data.datasource

import com.ighorosipov.data.model.Group
import com.ighorosipov.data.model.GroupWithMessages
import com.ighorosipov.data.model.Message

interface GroupDataSource {

    suspend fun createGroup(group: Group)

    suspend fun getGroups(userlogin: String): List<Group>

    suspend fun getAllMessagesFromGroups(userlogin: String): List<GroupWithMessages>

    suspend fun updateGroup(group: Group)

    suspend fun getGroupByName(groupName: String): Group?

    suspend fun deleteGroup(group: Group)

    suspend fun subscribeToGroup()

    suspend fun unsubscribeFromGroup()

}