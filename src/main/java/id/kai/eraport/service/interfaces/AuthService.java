package id.kai.eraport.service.interfaces;

import id.kai.eraport.dto.auth.LoginResponse;
import id.kai.eraport.dto.auth.RegisterUserRequest;
import id.kai.eraport.model.Users;

public interface AuthService {
    LoginResponse login(String email, String password);
    Users register(RegisterUserRequest request);
    LoginResponse refreshToken(String refreshToken);
}
