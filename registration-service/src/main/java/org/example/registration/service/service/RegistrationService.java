package org.example.registration.service.service;

import org.example.registration.service.dto.RegistrationRequestDto;
import org.example.registration.service.dto.RegistrationResponseDto;

public interface RegistrationService {
    RegistrationResponseDto registerParticipant(RegistrationRequestDto registrationRequestDto);
    void cancelRegistration(Long registrationId);
}
