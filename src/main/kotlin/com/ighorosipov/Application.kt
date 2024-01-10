package com.ighorosipov

import com.ighorosipov.data.PostgresDbFactory
import com.ighorosipov.data.datasource.postgresdatasource.PostgresUserDataSource
import com.ighorosipov.data.model.table.UserTable
import com.ighorosipov.di.mainModule
import com.ighorosipov.plugins.*
import com.ighorosipov.security.hashing.SHA256HashingService
import com.ighorosipov.security.token.JwtTokenService
import com.ighorosipov.security.token.TokenConfig
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }
    PostgresDbFactory.init()
    val userDataSource = PostgresUserDataSource()
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()
    configureSockets()
    configureSecurity(tokenConfig)
    configureRouting(
        hashingService = hashingService,
        userDataSource = userDataSource,
        tokenService = tokenService,
        tokenConfig = tokenConfig
    )
    configureMonitoring()
    configureSerialization()
}
