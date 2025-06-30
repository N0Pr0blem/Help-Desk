package com.praktica.HelpDesk.controller;

import com.praktica.HelpDesk.dto.user.UserResponseDto;
import com.praktica.HelpDesk.dto.user.UserUpdateDto;
import com.praktica.HelpDesk.mapper.UserMapper;
import com.praktica.HelpDesk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class ProfileController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PatchMapping()
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto usesUpdateDto, Principal principal) {
        return ResponseEntity.ok(userMapper.toDto(userService.updateUser(usesUpdateDto,principal)));
    }

    @GetMapping()
    public ResponseEntity<UserResponseDto> getUserProfile(Principal principal){
        return ResponseEntity.ok(userMapper.toDto(userService.getProfile(principal)));
    }

}
