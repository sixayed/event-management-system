package org.example.registration.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.registration.service.dto.RegistrationRequestDto;
import org.example.registration.service.dto.RegistrationResponseDto;
import org.example.registration.service.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerParticipant(@RequestBody @Valid RegistrationRequestDto registrationRequestDto) {
        RegistrationResponseDto registrationResponseDto = registrationService.registerParticipant(registrationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponseDto);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long id) {
        registrationService.cancelRegistration(id);
    }
}
