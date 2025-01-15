package com.hanaro.endingcredits.endingcreditsapi.utils.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.JwtHandler;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.InvalidJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class JwtProvider {
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private static final String SUBJECT = "88hana";

    private final long accessTokenExpirationInterval;
    private final long refreshTokenExpirationInterval;

    private static final String TOKEN_TYPE = "type";
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    public JwtProvider(long accessTokenExpirationInterval, long refreshTokenExpirationInterval) {
        this.accessTokenExpirationInterval = accessTokenExpirationInterval;
        this.refreshTokenExpirationInterval = refreshTokenExpirationInterval;
    }

    public String generateAccessToken(Map<String, ?> claims, Date date) {
        HashMap<String, Object> claimsMap = new HashMap<>(claims);
        claimsMap.put(TOKEN_TYPE, ACCESS_TOKEN);
        return generateToken(claimsMap, date, accessTokenExpirationInterval);
    }

    public String generateRefreshToken(Map<String, ?> claims, Date date) {
        HashMap<String, Object> claimsMap = new HashMap<>(claims);
        claimsMap.put(TOKEN_TYPE, REFRESH_TOKEN);
        return generateToken(claimsMap, date, refreshTokenExpirationInterval);
    }

    private String generateToken(Map<String, ?> claims, Date date, long interval) {
        Date expiresAt = new Date(date.getTime() + interval * 1000);
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(SUBJECT)
                .claims(claims)
                .issuedAt(date)
                .expiration(expiresAt)
                .signWith(SECRET_KEY)
                .compact();
    }

    public Map<String, ?> parseAccessToken(String token) throws InvalidJwtException {
        Map<String, ?> claims = parseClaims(token);
        if (!claims.get(TOKEN_TYPE).equals(ACCESS_TOKEN)) {
            throw new JwtHandler(ErrorStatus.INVALID_TOKEN);
        }
        return claims;
    }

    public Map<String, ?> parseRefreshToken(String token) throws InvalidJwtException {
        Map<String, ?> claims = parseClaims(token);
        if (!claims.get(TOKEN_TYPE).equals(REFRESH_TOKEN)) {
            throw new JwtHandler(ErrorStatus.INVALID_TOKEN);
        }
        return claims;
    }

    public long getRefreshTokenExpirationInterval() {
        return refreshTokenExpirationInterval;
    }

    public long getAccessTokenExpirationInterval() {
        return accessTokenExpirationInterval;
    }

    private Map<String, ?> parseClaims(String token) throws InvalidJwtException {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtHandler(ErrorStatus.INVALID_TOKEN);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
