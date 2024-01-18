package com.ighorosipov.data.model.table

import org.jetbrains.exposed.dao.id.UUIDTable

object GroupTable: UUIDTable("group", "uuid") {
    val name = varchar("group_name", 100)
    val owner = reference("owner_userlogin", UserTable.userlogin)
    val createdAt = long("created_at")
}