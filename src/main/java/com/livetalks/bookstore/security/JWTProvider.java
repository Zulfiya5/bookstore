package com.livetalks.bookstore.security;

import com.livetalks.bookstore.exception.RestException;
import com.livetalks.bookstore.exception.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTProvider {

    @Value(value = "${accessTokenLifeTime}")
    private Long accessTokenLifeTime;

    @Value(value = "${refreshTokenLifeTime}")
    private Long refreshTokenLifeTime;

    @Value(value = "${tokenSecretKey}")
    private String tokenSecretKey;

    public String generateTokenFromId(UUID id, boolean isAccess) {
        Date expiredDate = new Date(System.currentTimeMillis() + (isAccess ? accessTokenLifeTime : refreshTokenLifeTime));
        return "Bearer " + Jwts
                .builder()
                .setSubject(id.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecretKey)
                .compact();
    }

    public String getIdFromToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(tokenSecretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException();
        } catch (Exception e) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    public void validateToken(String token) {
        Jwts
                .parser()
                .setSigningKey(tokenSecretKey)
                .parseClaimsJws(token);
    }
}
