package com.ighorosipov.data.model.table

import org.jetbrains.exposed.dao.id.UUIDTable

object GroupTable: UUIDTable("group", "uuid") {
    val name = varchar("name", 100)
    val owner = reference("owner_login", UserTable.userlogin)
    val createdAt = long("created_at")
}