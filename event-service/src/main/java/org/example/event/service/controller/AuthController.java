package org.example.event.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.event.service.dto.user.AuthMapper;
import org.example.event.service.dto.user.LoginDto;
import org.example.event.service.dto.user.RegisterDto;
import org.example.event.service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        authService.register(registerDto);
        String token = authService.login(authMapper.registerToLoginDto(registerDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid LoginDto loginDto) {
        String token  = authService.login(loginDto);
        return ResponseEntity.ok(token);
    }
}
