package com.ighorosipov.di

import com.ighorosipov.data.PostgresUserDataSource
import com.ighorosipov.data.UserDataSource
import com.ighorosipov.room.RoomController
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val mainModule = module {
//    single {
//        KMongo.createClient()
//            .coroutine
//            .getDatabase("message_db")
//    }
//    single<MessageDataSource> {
//        MessageDataSourceImpl(get())
//    }

//    single {
//        Database.connect(HikariDataSource(
//            HikariConfig().apply {
//                driverClassName = System.getenv("JDBC_DRIVER")
//                jdbcUrl = System.getenv("JDBC_DATABASE_URL")
//                maximumPoolSize = 100
//                isAutoCommit = false
//                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
//                validate()
//            }
//        )
//        )
//    }

//    single<UserDataSource> {
//        PostgresUserDataSource(get())
//    }

//    single {
//        RoomController(get())
//    }
}