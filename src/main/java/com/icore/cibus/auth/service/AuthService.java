package com.icore.cibus.auth.service;

import com.icore.cibus.auth.dto.request.LoginRequest;
import com.icore.cibus.auth.dto.request.RegisterRequest;
import com.icore.cibus.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);
}
