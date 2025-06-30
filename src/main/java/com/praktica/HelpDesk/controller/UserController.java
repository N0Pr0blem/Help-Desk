package com.praktica.HelpDesk.controller;

import com.praktica.HelpDesk.dto.user.UserRequestDto;
import com.praktica.HelpDesk.dto.user.UserResponseDto;
import com.praktica.HelpDesk.dto.user.UserUpdateDto;
import com.praktica.HelpDesk.entity.Role;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.mapper.UserMapper;
import com.praktica.HelpDesk.secutiry.Encoder;
import com.praktica.HelpDesk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Encoder encoder;
    private final UserMapper userMapper;

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAll(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name="role", required = false) String role
    ) {
        List<UserEntity> res;
        if (email != null) res = List.of(userService.getByEmail(email));
        else if(role!=null) res = userService.getAllByRole(role);
        else if (page != null && size != null) res = userService.getAll(page, size);
        else res = userService.getAll();

        return ResponseEntity.ok(userMapper.toDtos(res));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(userMapper.toDto(userService.getById(id)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteById(@PathVariable("userId") Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User with id: " + id + " successfully deleted");
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable("userId") Long id,
            @Valid @RequestBody UserUpdateDto usesUpdateDto
    ) {
        return ResponseEntity.ok(userMapper.toDto(userService.updateUser(id, usesUpdateDto)));
    }

    @PatchMapping("/active/{userId}")
    public ResponseEntity<UserResponseDto> changeActive(
            @PathVariable("userId") Long userId,
            @RequestParam boolean isActive,
            Principal principal
    ) {
        return ResponseEntity.ok(userMapper.toDto(userService.changeActive(isActive, userId, principal)));
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createCustomUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userMapper.toDto(
                userService.registerUser(UserEntity.builder()
                        .isActive(true)
                        .role(Role.fromString(userRequestDto.getRole()))
                        .firstName(userRequestDto.getFirstName())
                        .secondName(userRequestDto.getSecondName())
                        .lastName(userRequestDto.getLastName())
                        .password(encoder.encode(userRequestDto.getPassword()))
                        .email(userRequestDto.getEmail())
                        .build())));
    }
}
