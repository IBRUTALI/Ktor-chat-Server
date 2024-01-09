package com.ighorosipov.data.model.table

import org.jetbrains.exposed.sql.Table

object GroupTable: Table() {

    val id = uuid("group_id").entityId()
    val name = varchar("group_name", 100)
    val arrayOfSubscribers = varchar("array_of_subscrubers", Int.MAX_VALUE)
    val owner = reference("userlogin", UserTable.userlogin)
    val createdAt = long("timestamp")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

}