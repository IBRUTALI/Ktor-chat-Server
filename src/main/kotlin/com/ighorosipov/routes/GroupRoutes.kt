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

const val GROUP_ID = "groupId"
fun Route.createGroup(
    groupDataSource: GroupDataSource
) {
    authenticate {
        post("groups/create-group") {
            val request = call.receiveOrNull<GroupRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val areFieldsBlank = request.name.isBlank()
            if (areFieldsBlank) {
                call.respond(HttpStatusCode.Conflict)
                return@post
            }

            val group = Group(
                name = request.name,
                owner = call.principal<JWTPrincipal>()?.getClaim(USERLOGIN, String::class)!!,
                createdAt = System.currentTimeMillis()
            )
            groupDataSource.createGroup(group)
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.subscribeToGroup(groupDataSource: GroupDataSource) {
    authenticate {
        post("{$GROUP_ID}/subscribe-to-group") {
            call.principal<JWTPrincipal>()?.getClaim(USERLOGIN, String::class)?.let { login ->
                val groupId = call.parameters[GROUP_ID].toString()
                groupDataSource.getGroupById(groupId)?.let {
                    val wasAcknowledge = groupDataSource.subscribeToGroup(
                        userlogin = login,
                        groupId = groupId
                    )
                    if (!wasAcknowledge) {
                        call.respond(HttpStatusCode.Conflict, "User is already subscribed on group with groupId: $groupId")
                        return@post
                    }
                    call.respond(HttpStatusCode.OK)
                    return@post
                }
                call.respond(HttpStatusCode.BadRequest, "Group not found")
            }
        }
    }
}

fun Route.unsubscribeFromGroup(groupDataSource: GroupDataSource) {
    authenticate {
        post("{$GROUP_ID}/unsubscribe-from-group") {
            call.principal<JWTPrincipal>()?.getClaim(USERLOGIN, String::class)?.let { login ->
                val groupId = call.parameters[GROUP_ID].toString()
                groupDataSource.getGroupById(groupId)?.let {
                    val wasAcknowledge = groupDataSource.unsubscribeFromGroup(
                        userlogin = login,
                        groupId = groupId
                    )
                    if (!wasAcknowledge) {
                        call.respond(HttpStatusCode.Conflict, "User is already unsubscribed from group with groupId: $groupId")
                        return@post
                    }
                    call.respond(HttpStatusCode.OK)
                    return@post
                }
                call.respond(HttpStatusCode.BadRequest, "Group not found")
            }
        }
    }
}