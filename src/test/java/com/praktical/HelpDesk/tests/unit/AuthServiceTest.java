package com.praktical.HelpDesk.tests.unit;

import com.praktica.HelpDesk.dto.auth.*;
import com.praktica.HelpDesk.entity.*;
import com.praktica.HelpDesk.secutiry.*;
import com.praktica.HelpDesk.secutiry.impl.*;
import com.praktica.HelpDesk.service.*;
import java.util.Date;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private AuthServiceImpl authService;

    private final String testEmail = "test@example.com";
    private final String testPassword = "validPassword123";
    private final String encodedPassword = "encodedPassword123";

    @Test
    void registerUser_ShouldCreateUser() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setEmail(testEmail);
        request.setPassword(testPassword);

        UserEntity user = UserEntity.builder()
                .email(testEmail)
                .password(encodedPassword)
                .build();

        when(passwordEncoder.encode(testPassword)).thenReturn(encodedPassword);
        when(userService.registerUser(any(UserEntity.class))).thenReturn(user);

        UserEntity result = authService.registerUser(request);

        assertNotNull(result);
        assertEquals(testEmail, result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
    }

    @Test
    void authenticateUser_ShouldReturnToken() {
        AuthRequestDto request = new AuthRequestDto();
        request.setEmail(testEmail);
        request.setPassword(testPassword);

        TokenDetails tokenDetails = new TokenDetails(
                1L,
                "test.token",
                new Date(),
                new Date(System.currentTimeMillis() + 3600 * 1000),
                Role.USER.name()         // <- Конвертируем enum в строку
        );

        when(securityService.authenticate(testEmail, testPassword))
                .thenReturn(tokenDetails);

        AuthResponseDto response = authService.authenticateUser(request);

        assertNotNull(response);
        assertEquals("test.token", response.getToken());
        assertEquals("USER", response.getRole());  // <- Сравниваем с String
    }

    @Test
    void activateUser_ShouldCallService() {
        String code = "activation-code";
        authService.activateUser(code);
        verify(userService).activateUser(code);
    }

    @Test
    void authenticateUser_InvalidCredentials_ShouldThrowException() {
        AuthRequestDto request = new AuthRequestDto();
        request.setEmail("wrong@example.com");
        request.setPassword("invalidPassword");

        when(securityService.authenticate("wrong@example.com", "invalidPassword"))
                .thenThrow(new SecurityException("Invalid credentials"));

        Exception exception = assertThrows(SecurityException.class, () -> {
            authService.authenticateUser(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }
}