package com.ighorosipov.data.model.table

import com.ighorosipov.data.model.table.MessageTable.autoGenerate
import org.jetbrains.exposed.sql.Table

object GroupTable: Table() {

    val id = uuid("group_id").autoGenerate()
    val name = varchar("group_name", 100)
    val owner = reference("userlogin", UserTable.userlogin)
    val createdAt = long("timestamp")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

}