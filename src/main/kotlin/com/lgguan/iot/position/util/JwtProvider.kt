package com.lgguan.iot.position.util

import arrow.core.getOrElse
import com.lgguan.iot.position.bean.BaseToken
import com.lgguan.iot.position.bean.ClaimMap
import com.lgguan.iot.position.exception.TokenGenerateException
import com.lgguan.iot.position.exception.TokenVerifyException
import io.github.nefilim.kjwt.*
import java.time.LocalDateTime

/**
 *
 *
 * @author N.Liu
 **/

const val BEARER_PREFIX = "Bearer "
private const val JWT_KEY_ID = "lgguan"
private const val SECRET = "lgguan-iot"
private const val ISSUER = "Indoor Positioning System"

enum class TokenType {
    Access {
        override fun subject() = "Access token"
        override fun expireTime() = 24 * 60 * 60L
    },
    Refresh {
        override fun subject() = "Refresh token"
        override fun expireTime() = 30 * 24 * 60 * 60L
    };

    abstract fun subject(): String
    abstract fun expireTime(): Long
}

fun ClaimMap.createToken(type: TokenType = TokenType.Access, now: LocalDateTime = LocalDateTime.now()): BaseToken {
    val token = generateToken(now, this, type)
    return if (token != null) {
        BaseToken(token, type.expireTime())
    } else throw TokenGenerateException()
}

fun String.refresh() =
    if (this.verify(TokenType.Refresh)) {
        val claimMap = ClaimMap()
        JWT.decodeT(this, JWSHMAC256Algorithm).tap {
            claimMap.keys.forEach { key ->
                claimMap[key] = it.claimValue(key).getOrElse { "" }
            }
        }
        Pair(claimMap.createToken(TokenType.Access), claimMap.createToken(TokenType.Refresh))
    } else throw TokenVerifyException()

fun String.verify(type: TokenType = TokenType.Access) =
    verify<JWSHMAC256Algorithm>(this, SECRET) { claims ->
        ClaimsVerification.validateClaims(
            ClaimsVerification.expired,
            ClaimsVerification.issuer(ISSUER),
            ClaimsVerification.subject(type.subject())
        )(claims)
    }.isValid

private fun generateToken(
    now: LocalDateTime,
    claimMap: ClaimMap,
    type: TokenType = TokenType.Access
) = JWT.hs256(JWTKeyID(JWT_KEY_ID)) {
    issuer(ISSUER)
    subject(type.subject())
    expiresAt(now.plusSeconds(type.expireTime()))
    claimMap.forEach { (k, v) ->
        claim(k, v)
    }
}.sign(SECRET).orNull()?.rendered

infix fun String.getValueOfKey(key: String): String? {
    var value: String? = null
    JWT.decodeT(this, JWSHMAC256Algorithm).tap {
        value = it.claimValue(key).orNull()
    }
    return value
}
