package com.praktica.HelpDesk.service.impl;

import com.praktica.HelpDesk.dto.user.UserUpdateDto;
import com.praktica.HelpDesk.entity.Role;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.exception.AuthException;
import com.praktica.HelpDesk.exception.UserException;
import com.praktica.HelpDesk.repository.UserRepository;
import com.praktica.HelpDesk.service.MailService;
import com.praktica.HelpDesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
                .orElseThrow(() -> new UserException("Such user doesn't exist", "NO_SUCH_USER_EXCEPTION"));
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Such user doesn't exist", "NO_SUCH_USER_EXCEPTION"));
    }

    @Override
    public UserEntity updateUser(Long id, UserUpdateDto userUpdateDto) {
        UserEntity user = getById(id);
        if (userUpdateDto.getFirstName() != null) user.setFirstName(userUpdateDto.getFirstName());
        if (userUpdateDto.getSecondName() != null) user.setSecondName(userUpdateDto.getSecondName());
        if (userUpdateDto.getLastName() != null) user.setLastName(userUpdateDto.getLastName());
        if (userUpdateDto.getPassword() != null) user.setPassword(userUpdateDto.getPassword());

        return userRepository.save(user);
    }

    @Override
    public UserEntity updateUser(UserUpdateDto userUpdateDto, Principal principal) {
        UserEntity user = getByEmail(principal.getName());

        return updateUser(user.getId(), userUpdateDto);
    }

    @Override
    public UserEntity registerUser(UserEntity userEntity) {

        if(userRepository.findByEmail(userEntity.getEmail()).isEmpty()) {
            return userRepository.save(userEntity);
        }
        else throw new AuthException("User already exist","USER_EXIST_EXCEPTION");
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

    @Override
    public UserEntity getProfile(Principal principal) {
        UserEntity user = getByEmail(principal.getName());
        return getById(user.getId());
    }

    @Override
    public UserEntity changeActive(boolean isActive, Long userId, Principal principal) {
        UserEntity admin = getByEmail(principal.getName());
        if(!admin.getId().equals(userId)) {
            UserEntity user = getById(userId);
            user.setActive(isActive);

            return userRepository.save(user);
        }
        else throw new UserException("You can't change your activity","");
    }

    @Override
    public List<UserEntity> getAllByRole(String role) {
        return userRepository.findAllByRole(Role.fromString(role).name());
    }
}
