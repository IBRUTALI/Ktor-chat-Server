package com.ighorosipov.data.datasource

import com.ighorosipov.data.model.Group
import com.ighorosipov.data.model.GroupWithMessages

interface GroupDataSource {

    suspend fun createGroup(group: Group)

    suspend fun getAllGroupsInfo(userlogin: String): List<GroupWithMessages>

    suspend fun getGroupInfo(userlogin: String): GroupWithMessages

    suspend fun updateGroup(group: Group)

    suspend fun getGroupByName(groupName: String): Group?

    suspend fun getGroupById(groupId: String): Group?

    suspend fun deleteGroup(group: Group)

    suspend fun subscribeToGroup(userlogin: String, groupId: String): Boolean

    suspend fun unsubscribeFromGroup(userlogin: String, groupId: String): Boolean

}