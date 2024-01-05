package com.ighorosipov

import com.ighorosipov.data.PostgresDbFactory
import com.ighorosipov.di.mainModule
import com.ighorosipov.plugins.*
import io.ktor.server.application.*
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }
    PostgresDbFactory.init()
    configureSerialization()
    configureMonitoring()
    configureSockets()
    configureRouting()
    configureSecurity()
}
