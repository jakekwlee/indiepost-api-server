package com.indiepost.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.io.UnsupportedEncodingException

object TokenValidator {

    @Throws(UnsupportedEncodingException::class)
    fun validate(accessToken: String, clientSecret: String): DecodedJWT {
        val algorithm = Algorithm.HMAC256(clientSecret)
        val verifier = JWT.require(algorithm)
                .withIssuer("https://indiepost.auth0.com/")
                .withAudience("https://www.indiepost.co.kr/api")
                .build() //Reusable verifier instance
        return verifier.verify(accessToken)
    }
}
