package com.sourav.taskflow.service;

import com.sourav.taskflow.dto.auth.AuthResponse;
import com.sourav.taskflow.dto.auth.LoginRequest;
import com.sourav.taskflow.dto.auth.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
