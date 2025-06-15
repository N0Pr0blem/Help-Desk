package com.praktica.HelpDesk.service.impl;

import com.praktica.HelpDesk.dto.user.RegisterUserDto;
import com.praktica.HelpDesk.dto.user.UpdateUserDto;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.exception.UserException;
import com.praktica.HelpDesk.repository.UserRepository;
import com.praktica.HelpDesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserEntity> getAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).stream().toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.delete(getById(id));
    }

    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new UserException("Such user doesn't exist", "NO_SUCH_USER_EXCEPTION"));
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Such user doesn't exist", "NO_SUCH_USER_EXCEPTION"));
    }

    @Override
    public UserEntity updateUser(Long id, UpdateUserDto updateUserDto) {
        UserEntity user = getById(id);
        if(updateUserDto.getFirstName()!=null) user.setFirstName(user.getFirstName());
        if(updateUserDto.getSecondName()!=null) user.setSecondName(user.getSecondName());
        if(updateUserDto.getLastName()!=null) user.setLastName(user.getLastName());
        if(updateUserDto.getPassword()!=null) user.setPassword(user.getPassword());

        return userRepository.save(user);
    }

    @Override
    public UserEntity registerUser(RegisterUserDto registerUserDto) {
        return userRepository.save(UserEntity.builder()
                .email(registerUserDto.getEmail())
                .password(registerUserDto.getPassword())
                .firstName(registerUserDto.getFirstName())
                .secondName(registerUserDto.getSecondName())
                .lastName(registerUserDto.getLastName())
                .isActive(false)
                .build());
    }

    @Override
    public void activateUser(String code) {
        userRepository.findByActivationCode(code)
                .ifPresentOrElse(userEntity -> {
                    userEntity.setActive(true);
                    userEntity.setActivationCode("");
                    userRepository.save(userEntity);
                }, () -> {
                    throw new UserException("Wrong activation code", "USER_ACTIVATION_CODE_EXCEPTION");
                });
    }
}
