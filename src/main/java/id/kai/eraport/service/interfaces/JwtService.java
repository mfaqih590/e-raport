package id.kai.eraport.service.interfaces;

import id.kai.eraport.dto.auth.JwtUserInfo;
import id.kai.eraport.model.Users;
import io.jsonwebtoken.Claims;

public interface JwtService {
    String generateAccessToken(Users users);
    String generateRefreshToken(Users users);
    Claims extractAllClaims(String token);
    JwtUserInfo extractUserInfo(String token);
    String extractEmail(String token);
    boolean isTokenValid(String token);
}
