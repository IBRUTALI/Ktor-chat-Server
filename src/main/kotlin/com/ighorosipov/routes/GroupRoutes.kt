package com.ighorosipov.routes

import io.ktor.server.routing.*

fun Route.createGroup(

) {
    fun Route.getGroups() {
        get("/groups") {

        }
    }

    post("/groups/create-group") {

    }
}

fun Route.joinGroup() {
    post("/groups/groupId={groupId}/join-group") {

    }
}

fun Route.subscribeOnGroup() {
    post("/groups/groupId={groupId}/subscribe-to-group") {

    }
}

fun Route.unsubscribeFromGroup() {
    post("/groups/groupId={groupId}/unsubscribe-from-group") {

    }
}