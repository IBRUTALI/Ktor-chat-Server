package com.ighorosipov.data.model.table

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {

    val userlogin = varchar("userlogin", 512)
    val username = varchar("username", 100)
    val avatarUrl = varchar("avatar_url", 512)
    val password = varchar("password", 512)
    val salt = varchar("salt", 512)

    override val primaryKey: PrimaryKey = PrimaryKey(userlogin)
}