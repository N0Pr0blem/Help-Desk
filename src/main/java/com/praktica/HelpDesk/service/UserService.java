package com.praktica.HelpDesk.service;

import com.praktica.HelpDesk.dto.user.UpdateUserDto;
import com.praktica.HelpDesk.dto.user.RegisterUserDto;
import com.praktica.HelpDesk.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> getAll();

    List<UserEntity> getAll(int page,int size);

    void deleteById(Long id);

    UserEntity getById(Long id);

    UserEntity getByEmail(String email);

    UserEntity updateUser(Long id, UpdateUserDto updateUserDto);

    UserEntity registerUser(RegisterUserDto registerUserDto);

    void activateUser(String code);
}
