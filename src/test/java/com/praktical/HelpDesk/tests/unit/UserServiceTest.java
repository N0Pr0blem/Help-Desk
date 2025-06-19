package com.praktical.HelpDesk.tests.unit;

import com.praktica.HelpDesk.repository.UserRepository;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.service.UserService;
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
    private UserService userService;

    @Test
    public void getUserById_ShouldReturnUser() {
        UserEntity mockUser = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        UserEntity result = userService.getById(1L);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());  
    }
}
