package id.kai.eraport.controller;

import id.kai.eraport.common.helper.ResponseBuilder;
import id.kai.eraport.common.response.ApiResponse;
import id.kai.eraport.dto.auth.LoginRequest;
import id.kai.eraport.dto.auth.LoginResponse;
import id.kai.eraport.dto.auth.RefreshTokenRequest;
import id.kai.eraport.dto.auth.RegisterUserRequest;
import id.kai.eraport.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@Validated @RequestBody RegisterUserRequest request) {
        authService.register(request);

        return ResponseBuilder.created("Form created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Validated @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getEmail(), request.getPassword());

        return ResponseBuilder.ok(response, "Login success");
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request.getRefreshToken());

        return ResponseBuilder.ok(response, "Refresh Success");
    }
}
