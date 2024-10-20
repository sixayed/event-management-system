package org.example.event.service.service;

import org.example.event.service.dto.user.LoginDto;
import org.example.event.service.dto.user.RegisterDto;

public interface AuthService {
    void register(RegisterDto registerDto);
    String login(LoginDto loginDto);
}