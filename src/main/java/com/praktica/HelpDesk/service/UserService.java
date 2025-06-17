package com.praktica.HelpDesk.service;

import com.praktica.HelpDesk.dto.user.UserUpdateDto;
import com.praktica.HelpDesk.entity.UserEntity;

import java.security.Principal;
import java.util.List;

public interface UserService {
    List<UserEntity> getAll();

    List<UserEntity> getAll(int page, int size);

    void deleteById(Long id);

    UserEntity getById(Long id);

    UserEntity getByEmail(String email);

    UserEntity updateUser(Long id, UserUpdateDto userUpdateDto);

    UserEntity updateUser(UserUpdateDto userUpdateDto, Principal principal);

    UserEntity registerUser(UserEntity userEntity);

    void activateUser(String code);

    UserEntity getProfile(Principal principal);

    UserEntity changeActive(boolean isActive, Long userId,Principal principal);
}
