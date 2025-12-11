package id.kai.eraport.service.impl;

import id.kai.eraport.dto.auth.LoginResponse;
import id.kai.eraport.dto.auth.RegisterUserRequest;
import id.kai.eraport.exception.DuplicateResourceException;
import id.kai.eraport.exception.InvalidCredentialsException;
import id.kai.eraport.exception.ResourceNotFoundException;
import id.kai.eraport.exception.UnauthorizedException;
import id.kai.eraport.model.Roles;
import id.kai.eraport.model.Users;
import id.kai.eraport.repository.db.RoleRepository;
import id.kai.eraport.repository.db.UserRepository;
import id.kai.eraport.service.interfaces.AuthService;
import id.kai.eraport.service.interfaces.JwtService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse login(String email, String password) {
        Users user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);

        return loginResponse;
    }

    public Users register(RegisterUserRequest request) {
        Users users = new Users();
        users.setName(request.getName());
        if (userRepository.findByEmailAndIsDeletedFalse(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already registered!");
        }
        Roles roles = roleRepository.findByIdAndIsDeletedFalse(request.getRole()).orElseThrow(()
                -> new ResourceNotFoundException("Role not found!"));
        users.setRole(roles.getId());
        users.setEmail(request.getEmail());
        users.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setDeleted(false);
        userRepository.save(users);

        return users;
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String email = jwtService.extractEmail(refreshToken);

        Users user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(newAccessToken);
        loginResponse.setRefreshToken(newRefreshToken);

        return loginResponse;
    }
}
