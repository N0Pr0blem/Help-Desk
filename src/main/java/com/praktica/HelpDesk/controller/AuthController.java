package com.praktica.HelpDesk.controller;

import com.praktica.HelpDesk.dto.auth.AuthRequestDto;
import com.praktica.HelpDesk.dto.auth.AuthResponseDto;
import com.praktica.HelpDesk.dto.auth.RegisterRequestDto;
import com.praktica.HelpDesk.dto.user.UserResponseDto;
import com.praktica.HelpDesk.mapper.UserMapper;
import com.praktica.HelpDesk.secutiry.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto){
        return ResponseEntity.ok(userMapper.toDto(authService.registerUser(registerRequestDto)));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    public AuthResponseDto login(@RequestBody AuthRequestDto authRequestDto){
        return authService.authenticateUser(authRequestDto);
    }

}
