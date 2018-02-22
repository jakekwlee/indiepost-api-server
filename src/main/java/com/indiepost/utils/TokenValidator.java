package com.indiepost.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;

public class TokenValidator {

    public DecodedJWT validate(String accessToken, String clientSecret) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(clientSecret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("https://indiepost.auth0.com/")
                .withAudience("https://www.indiepost.co.kr/api")
                .build(); //Reusable verifier instance
        return verifier.verify(accessToken);
    }
}
