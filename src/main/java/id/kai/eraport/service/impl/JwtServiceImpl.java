package id.kai.eraport.service.impl;

import id.kai.eraport.dto.auth.JwtUserInfo;
import id.kai.eraport.model.Users;
import id.kai.eraport.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
@Transactional
public class JwtServiceImpl implements JwtService {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME_ACCESS_TOKEN = 1000 * 60 * 60 * 24;
    private static final long EXPIRATION_TIME_REFRESH_TOKEN = 1000L * 60 * 60 * 24 * 365;

    public String generateAccessToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS_TOKEN))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Users user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_REFRESH_TOKEN)) // contoh 7 hari
                .signWith(key)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public JwtUserInfo extractUserInfo(String token) {
        Claims claims = extractAllClaims(token);
        UUID id = Optional.ofNullable(claims.get("id", String.class))
                .map(UUID::fromString)
                .orElse(null);

        UUID role = Optional.ofNullable(claims.get("role", String.class))
                .map(UUID::fromString)
                .orElse(null);
        return new JwtUserInfo(
                id,
                claims.get("name", String.class),
                claims.get("email", String.class),
                role
        );
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
