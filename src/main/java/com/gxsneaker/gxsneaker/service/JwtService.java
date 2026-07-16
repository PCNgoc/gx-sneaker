package com.gxsneaker.gxsneaker.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtService {


    private final String SECRET_KEY =
            "GX_SNEAKER_SECRET_KEY_123456789_123456789";



    public String generateToken(String username, String role) {

        return Jwts.builder()

                .subject(username)

                .claim("role", role)

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 86400000
                        )
                )

                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes()
                        )
                )

                .compact();

    }



    public String extractUsername(String token) {

        return Jwts.parser()

                .verifyWith(
                        Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes()
                        )
                )

                .build()

                .parseSignedClaims(token)

                .getPayload()

                .getSubject();

    }



    // ================= CHECK TOKEN =================


    public boolean isTokenValid(

            String token,

            UserDetails userDetails

    ){

        final String username =
                extractUsername(token);


        return username.equals(
                userDetails.getUsername()
        );

    }

    public String extractRole(String token) {

        return Jwts.parser()
                .verifyWith(
                        Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes()
                        )
                )
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);

    }



}