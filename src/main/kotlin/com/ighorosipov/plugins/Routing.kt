package com.ighorosipov.plugins

import com.ighorosipov.data.datasource.GroupDataSource
import com.ighorosipov.data.datasource.MessageDataSource
import com.ighorosipov.data.datasource.UserDataSource
import com.ighorosipov.room.RoomController
import com.ighorosipov.routes.*
import com.ighorosipov.security.hashing.HashingService
import com.ighorosipov.security.token.TokenConfig
import com.ighorosipov.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    groupDataSource: GroupDataSource,
    messageDataSource: MessageDataSource
) {
    val roomController by inject<RoomController>()
    install(Routing) {
        chatSocket(roomController)
        getAllMessages(roomController)
        signUp(
            hashingService = hashingService,
            userDataSource = userDataSource
        )
        signIn(
            hashingService = hashingService,
            userDataSource = userDataSource,
            tokenService = tokenService,
            tokenConfig = tokenConfig
        )
        authenticate()
        getSecretInfo()
        createGroup(groupDataSource)
        getGroups(groupDataSource)
        joinGroup(groupDataSource)
        subscribeToGroup(groupDataSource)
        unsubscribeFromGroup(groupDataSource)
    }
}
