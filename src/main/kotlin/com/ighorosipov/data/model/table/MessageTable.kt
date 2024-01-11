package com.ighorosipov.data.model.table

import com.ighorosipov.data.model.table.GroupTable.entityId
import org.jetbrains.exposed.sql.Table

object MessageTable: Table() {

    val id = uuid("message_id").autoGenerate()
    val groupId = reference("group_id", GroupTable.id)
    val text = varchar("text", 512)
    val userlogin = reference("userlogin", UserTable.userlogin)
    val timestamp = long("timestamp")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

}