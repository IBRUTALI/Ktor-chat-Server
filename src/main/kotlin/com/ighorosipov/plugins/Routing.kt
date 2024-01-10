package com.ighorosipov.plugins

import com.ighorosipov.data.datasource.UserDataSource
import com.ighorosipov.routes.getSecretInfo
import com.ighorosipov.routes.authenticate
import com.ighorosipov.room.RoomController
import com.ighorosipov.routes.chatSocket
import com.ighorosipov.routes.getAllMessages
import com.ighorosipov.security.hashing.HashingService
import com.ighorosipov.security.token.TokenConfig
import com.ighorosipov.security.token.TokenService
import com.ighorosipov.routes.signIn
import com.ighorosipov.routes.signUp
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
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
    }
}
