package com.praktica.HelpDesk.controller;

import com.praktica.HelpDesk.dto.user.UserOutputDto;
import com.praktica.HelpDesk.dto.user.UserUpdateDto;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.mapper.UserMapper;
import com.praktica.HelpDesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping()
    public ResponseEntity<List<UserOutputDto>> getAll(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "email", required = false) String email
    ) {
        List<UserEntity> res;
        if (email != null) res = List.of(userService.getByEmail(email));
        else if (page != null && size != null) res = userService.getAll(page, size);
        else res = userService.getAll();

        return ResponseEntity.ok(userMapper.toDtos(res));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User with id: " + id + " successfully deleted");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserOutputDto> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UserUpdateDto usesUpdateDto
    ) {
        return ResponseEntity.ok(userMapper.toDto(userService.updateUser(id,usesUpdateDto)));
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("code") String code){
        userService.activateUser(code);
        return ResponseEntity.ok("User successfully activate");
    }
}
