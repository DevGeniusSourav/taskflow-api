package com.sourav.taskflow.service.impl;

import com.sourav.taskflow.dto.auth.AuthResponse;
import com.sourav.taskflow.dto.auth.LoginRequest;
import com.sourav.taskflow.dto.auth.RegisterRequest;
import com.sourav.taskflow.entity.User;
import com.sourav.taskflow.enums.Role;
import com.sourav.taskflow.repository.UserRepository;
import com.sourav.taskflow.security.JwtService;
import com.sourav.taskflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("Invalid Credentials!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials!");
        }

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }
}
