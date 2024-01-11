package com.ighorosipov.routes

import com.ighorosipov.data.datasource.UserDataSource
import com.ighorosipov.data.model.User
import com.ighorosipov.data.requests.AuthRequest
import com.ighorosipov.data.responses.AuthResponse
import com.ighorosipov.security.hashing.HashingService
import com.ighorosipov.security.hashing.SaltedHash
import com.ighorosipov.security.token.TokenClaim
import com.ighorosipov.security.token.TokenConfig
import com.ighorosipov.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("signup") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val areFieldsBlank = request.userlogin.isBlank() || request.password.isBlank() || request.username.isBlank()
        val isPasswordTooShort = request.password.length < 8
        if(areFieldsBlank || isPasswordTooShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            userlogin = request.userlogin,
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledge = userDataSource.insertNewUser(user)
        if (!wasAcknowledge) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByLogin(request.userlogin)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "User not found")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrect password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "login",
                value = user.userlogin
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userlogin = principal?.getClaim("login", String::class)
            call.respond(HttpStatusCode.OK, "Your login is $userlogin")
        }
    }
}