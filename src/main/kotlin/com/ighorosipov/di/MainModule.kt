package com.ighorosipov.di

import com.ighorosipov.data.datasource.GroupDataSource
import com.ighorosipov.data.datasource.MessageDataSource
import com.ighorosipov.data.datasource.postgresdatasource.PostgresUserDataSource
import com.ighorosipov.data.datasource.UserDataSource
import com.ighorosipov.data.datasource.postgresdatasource.PostgresGroupDataSource
import com.ighorosipov.data.datasource.postgresdatasource.PostgresMessageDataSource
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

//    single<MessageDataSource> {
//        PostgresMessageDataSource(get())
//    }
//
//    single<UserDataSource> {
//        PostgresUserDataSource(get())
//    }
//
//    single<GroupDataSource> {
//        PostgresGroupDataSource(get())
//    }

    single {
        RoomController(get())
    }

}