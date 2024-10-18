package org.example.event.service.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.service.dto.user.LoginDto;
import org.example.event.service.dto.user.RegisterDto;
import org.example.event.service.exception.EmailAlreadyExistsException;
import org.example.event.service.exception.UserAlreadyExistsException;
import org.example.event.service.model.UserEntity;
import org.example.event.service.repository.UserRepository;
import org.example.event.service.security.jwt.JwtTokenProvider;
import org.example.event.service.security.model.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(@NonNull RegisterDto registerDto) {
        if (!validateLogin(registerDto.getLogin())) {
            throw new InvalidParameterException("Invalid login");
        }
        if (!validateEmail(registerDto.getEmail())) {
            throw new InvalidParameterException("Invalid email");
        }
        if (!validatePassword(registerDto.getPassword())) {
            throw new InvalidParameterException("Invalid password");
        }

        if (userRepository.findByLogin(registerDto.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException("User with that login already exists");
        }
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("User with that email already exists");
        }

        UserEntity user = new UserEntity();
        user.setLogin(registerDto.getLogin());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        log.info("User registered {}", user);

    }

    @Override
    public String login(@NonNull LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtTokenProvider.generateToken(userDetails);
    }

    private boolean validateEmail(@NonNull String email) {
        return true;
    }

    private boolean validateLogin(@NonNull String login) {
        String loginRegex = "^[a-zA-Z0-9]{3,20}$";
        return Pattern.compile(loginRegex).matcher(login).matches();
    }

    private boolean validatePassword(@NonNull String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }
}
