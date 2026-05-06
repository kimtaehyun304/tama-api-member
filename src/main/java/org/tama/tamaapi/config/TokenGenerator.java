package org.tama.tamaapi.config;


import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;

import org.tama.sharelib.common.auth.jwt.JwtProperties;
import org.tama.tamaapi.domain.user.Member;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtProperties jwtProperties;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    public String generateTestToken(Member member) {
        Date now = new Date();
        return makeToken(member, new Date(now.getTime() + Duration.ofDays(365).toMillis()));
    }

    public String generateToken(Member member) {
        Date now = new Date();
        return makeToken(member, new Date(now.getTime() + ACCESS_TOKEN_DURATION.toMillis()));
    }

    private String makeToken(Member member, Date expiry) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(member.getId().toString())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

}
