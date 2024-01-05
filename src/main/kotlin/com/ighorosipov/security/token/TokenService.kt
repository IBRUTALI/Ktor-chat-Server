package com.ighorosipov.security.token

import com.ighorosipov.security.token.TokenClaim
import com.ighorosipov.security.token.TokenConfig

interface TokenService {

    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

}