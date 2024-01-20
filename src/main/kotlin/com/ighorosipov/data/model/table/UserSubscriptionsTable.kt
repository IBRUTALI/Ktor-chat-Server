package com.ighorosipov.data.model.table

import org.jetbrains.exposed.sql.Table

object UserSubscriptionsTable : Table("subscriptions") {

    val userlogin = reference("userlogin", UserTable.userlogin)
    val groupId = reference("group_id", GroupTable.id)
    val isSubscribed = bool("is_subscribed")

}