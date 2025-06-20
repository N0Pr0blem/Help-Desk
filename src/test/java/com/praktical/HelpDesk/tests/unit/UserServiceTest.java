package com.praktical.HelpDesk.tests.unit;

import com.praktica.HelpDesk.repository.UserRepository;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.service.UserService;
import com.praktica.HelpDesk.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserById_ShouldReturnUser() {

        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(1L);
        expectedUser.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        UserEntity result = userService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findById(1L);
    }
}