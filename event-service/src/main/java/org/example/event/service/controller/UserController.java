package org.example.event.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.event.service.dto.user.LoginDto;
import org.example.event.service.dto.user.RegisterDto;
import org.example.event.service.dto.user.UserMapper;
import org.example.event.service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        userService.register(registerDto);
        String token = userService.login(userMapper.registerToLoginDto(registerDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid LoginDto loginDto) {
        String token  = userService.login(loginDto);
        return ResponseEntity.ok(token);
    }
}
