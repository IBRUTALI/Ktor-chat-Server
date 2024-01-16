package com.ighorosipov.routes

import com.ighorosipov.data.datasource.GroupDataSource
import com.ighorosipov.data.model.Group
import com.ighorosipov.data.requests.GroupRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.createGroup(
    groupDataSource: GroupDataSource
) {
    authenticate {
        post("/groups/create-group") {
            val request = call.receiveOrNull<GroupRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val areFieldsBlank = request.name.isBlank() || request.owner.isBlank()
            if (areFieldsBlank) {
                call.respond(HttpStatusCode.Conflict)
                return@post
            }

            val group = Group(
                id = UUID.randomUUID().toString(),
                name = request.name,
                owner = call.principal<JWTPrincipal>()?.getClaim("login", String::class)!!,
                createdAt = System.currentTimeMillis()
            )
            groupDataSource.createGroup(group)
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getGroups(groupDataSource: GroupDataSource) {
    authenticate {
        get("/groups") {
            call.principal<JWTPrincipal>()?.getClaim("login", String::class)?.let {
                groupDataSource.getAllGroupsInfo(it)
            }
        }
    }
}

fun Route.joinGroup(groupDataSource: GroupDataSource) {
    post("/groups/groupId={groupId}/join-group") {

    }
}

fun Route.subscribeToGroup(groupDataSource: GroupDataSource) {
    authenticate {
        post("/groups/groupId={groupId}/subscribe-to-group") {
            call.principal<JWTPrincipal>()?.getClaim("login", String::class)?.let { login ->
                groupDataSource.subscribeToGroup(
                    userlogin = login,
                    groupId = call.parameters["groupId"].toString()
                )
            }
        }
    }
}

fun Route.unsubscribeFromGroup(groupDataSource: GroupDataSource) {
    authenticate {
        post("/groups/groupId={groupId}/unsubscribe-from-group") {
            call.principal<JWTPrincipal>()?.getClaim("login", String::class)?.let { login ->
                groupDataSource.unsubscribeFromGroup(
                    userlogin = login,
                    groupId = call.parameters["groupId"].toString()
                )
            }
        }
    }
}