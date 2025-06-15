package com.praktica.HelpDesk.service.impl;

import com.praktica.HelpDesk.dto.user.UserUpdateDto;
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
    public UserEntity updateUser(Long id, UserUpdateDto userUpdateDto) {
        UserEntity user = getById(id);
        if(userUpdateDto.getFirstName()!=null) user.setFirstName(userUpdateDto.getFirstName());
        if(userUpdateDto.getSecondName()!=null) user.setSecondName(userUpdateDto.getSecondName());
        if(userUpdateDto.getLastName()!=null) user.setLastName(userUpdateDto.getLastName());
        if(userUpdateDto.getPassword()!=null) user.setPassword(userUpdateDto.getPassword());

        return userRepository.save(user);
    }

    @Override
    public UserEntity registerUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public void activateUser(String code) {
        userRepository.findByActivationCode(code)
                .ifPresentOrElse(userEntity -> {
                    userEntity.setActive(true);
                    userEntity.setActivationCode(null);
                    userRepository.save(userEntity);
                }, () -> {
                    throw new UserException("Wrong activation code", "USER_ACTIVATION_CODE_EXCEPTION");
                });
    }
}
