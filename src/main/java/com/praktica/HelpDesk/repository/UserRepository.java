package com.praktica.HelpDesk.repository;

import com.praktica.HelpDesk.entity.Role;
import com.praktica.HelpDesk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByActivationCode(String code);

    @Query(value = """
            Select * from users where role=:role
            """,nativeQuery = true)
    List<UserEntity> findAllByRole(String role);
}
