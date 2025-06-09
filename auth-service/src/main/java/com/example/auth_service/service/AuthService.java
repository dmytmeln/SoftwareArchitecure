package com.example.auth_service.service;

import com.example.auth_service.domain.User;
import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.LoginResponse;
import com.example.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        var user = userService.findByEmail(loginRequest.email());
        verifyPasswordMatches(loginRequest, user);
        var jwtToken = jwtUtil.generateToken(user);
        return new LoginResponse(jwtToken, user.getEmail());
    }

    private void verifyPasswordMatches(LoginRequest loginRequest, User user) {
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}
