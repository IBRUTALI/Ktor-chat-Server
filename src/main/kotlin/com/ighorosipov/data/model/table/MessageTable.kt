package com.ighorosipov.data.model.table

import org.jetbrains.exposed.dao.id.UUIDTable

object MessageTable: UUIDTable("message", "uuid") {

    val groupId = reference("group_id", GroupTable.id)
    val text = varchar("text", 512)
    val userlogin = reference("userlogin", UserTable.userlogin)
    val isEdited = bool("is_edited")
    val timestamp = long("timestamp")

}