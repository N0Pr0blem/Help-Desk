package com.praktical.HelpDesk.tests.unit;

import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void findByEmail_ShouldReturnUserWhenExists() {
        // Arrange
        String testEmail = "test@example.com";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail(testEmail);

        when(userRepository.findByEmail(testEmail))
                .thenReturn(Optional.of(expectedUser));

        // Act
        Optional<UserEntity> result = userRepository.findByEmail(testEmail);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testEmail, result.get().getEmail());
        verify(userRepository, times(1)).findByEmail(testEmail);
    }

    @Test
    public void findByEmail_ShouldReturnEmptyWhenNotExists() {
        // Arrange
        String nonExistentEmail = "nonexistent@example.com";

        when(userRepository.findByEmail(nonExistentEmail))
                .thenReturn(Optional.empty());

        // Act
        Optional<UserEntity> result = userRepository.findByEmail(nonExistentEmail);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(nonExistentEmail);
    }

    @Test
    public void findByActivationCode_ShouldReturnUserWhenCodeExists() {
        // Arrange
        String testCode = "ACTIVATION-123";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setActivationCode(testCode);

        when(userRepository.findByActivationCode(testCode))
                .thenReturn(Optional.of(expectedUser));

        // Act
        Optional<UserEntity> result = userRepository.findByActivationCode(testCode);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testCode, result.get().getActivationCode());
        verify(userRepository, times(1)).findByActivationCode(testCode);
    }

    @Test
    public void saveUser_ShouldReturnSavedUser() {
        // Arrange
        UserEntity newUser = new UserEntity();
        newUser.setEmail("new@example.com");

        when(userRepository.save(newUser))
                .thenReturn(newUser);

        // Act
        UserEntity savedUser = userRepository.save(newUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals("new@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(newUser);
    }
}