package com.praktica.HelpDesk.secutiry.impl;

import com.praktica.HelpDesk.dto.auth.AuthRequestDto;
import com.praktica.HelpDesk.dto.auth.AuthResponseDto;
import com.praktica.HelpDesk.dto.auth.RegisterRequestDto;
import com.praktica.HelpDesk.dto.user.UserResponseDto;
import com.praktica.HelpDesk.entity.Role;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.exception.UserException;
import com.praktica.HelpDesk.secutiry.AuthService;
import com.praktica.HelpDesk.secutiry.SecurityService;
import com.praktica.HelpDesk.secutiry.TokenDetails;
import com.praktica.HelpDesk.service.MailService;
import com.praktica.HelpDesk.service.UserService;
import com.praktica.HelpDesk.util.ActivationCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    public UserEntity registerUser(RegisterRequestDto registerRequestDto) {
        String activationCode = ActivationCodeGenerator.generateCode();

        mailService.sendActivationCodeForm(registerRequestDto.getEmail(), activationCode);

        return userService.registerUser(UserEntity.builder()
                .email(registerRequestDto.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .activationCode(activationCode)
                .firstName(registerRequestDto.getFirstName())
                .secondName(registerRequestDto.getSecondName())
                .lastName(registerRequestDto.getLastName())
                .isActive(false)
                .build());
    }

    @Override
    public AuthResponseDto authenticateUser(AuthRequestDto authRequestDto) {
        TokenDetails tokenDetails = securityService.authenticate(authRequestDto.getEmail(), authRequestDto.getPassword());
        return AuthResponseDto.builder()
                .userId(tokenDetails.getId())
                .token(tokenDetails.getToken())
                .expiresAt(tokenDetails.getExpiredAt())
                .issuedAt(tokenDetails.getIssuedAt())
                .role(tokenDetails.getRole())
                .build();
    }

    @Override
    public void activateUser(String code) {
        userService.activateUser(code);
    }
}
