package com.praktica.HelpDesk.repository;

import com.praktica.HelpDesk.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByActivationCode(String code);
}
