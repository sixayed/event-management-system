package org.example.registration.service.service;

import lombok.RequiredArgsConstructor;
import org.example.registration.service.dto.RegistrationMapper;
import org.example.registration.service.dto.RegistrationRequestDto;
import org.example.registration.service.dto.RegistrationResponseDto;
import org.example.registration.service.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService{

    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;

    @Override
    public RegistrationResponseDto registerParticipant(RegistrationRequestDto registrationRequestDto) {
        return null;
    }

    @Override
    public void cancelRegistration(Long registrationId) {

    }
}
