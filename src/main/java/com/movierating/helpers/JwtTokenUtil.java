package com.movierating.helpers;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class JwtTokenUtil {
    @Value("jwt.secret")
    private String secret;
    private Algorithm algo;

    @PostConstruct
    public void setup() {
        algo = Algorithm.HMAC256(secret);
    }

    public String createToken(int id) {
        String token = JWT.create()
                .withIssuer("arcanecat.com")
                .withClaim("user_id", id)
                .sign(algo);
        return token;
    }

    public String decodeToken(String token) {
        DecodedJWT decodedJWT;
        try {
            JWTVerifier verifier = JWT.require(algo)
                    .withIssuer("arcanecat.com")
                    .build();
            decodedJWT = verifier.verify(token);
            String value = decodedJWT.getClaim("user_id").toString();
            if (value == null) {
                throw new IllegalArgumentException();
            }
            return value;
        } catch (JWTVerificationException exception) {
            throw exception;
        }
    }
}
