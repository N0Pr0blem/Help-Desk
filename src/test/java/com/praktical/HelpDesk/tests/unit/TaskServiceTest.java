package com.praktical.HelpDesk.tests.unit;

import com.praktica.HelpDesk.dto.task.TaskRequestDto;
import com.praktica.HelpDesk.entity.Task;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.repository.TaskRepository;
import com.praktica.HelpDesk.entity.TaskStatus;
import com.praktica.HelpDesk.service.UserService;
import com.praktica.HelpDesk.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private Principal principal;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task createTestTask() {
        Task task = new Task();
        task.setId(1L);
        task.setDescription("Test task");
        task.setStatus(TaskStatus.WAIT);
        task.setCreatedAt(LocalDateTime.now());

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        task.setToUser(user);

        return task;
    }

    @Test
    void getAll_ShouldReturnTasksList() {
        // Arrange
        Task task = createTestTask();
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));

        // Act
        List<Task> result = taskService.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test task", result.get(0).getDescription());
    }

    @Test
    void getById_ShouldReturnTask() {
        // Arrange
        Task task = createTestTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        Task result = taskService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void create_ShouldSaveNewTask() {
        // Arrange
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setDescription("New task");

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userService.getByEmail(anyString())).thenReturn(user);
        when(principal.getName()).thenReturn("test@example.com");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setDescription("New task");
        savedTask.setStatus(TaskStatus.WAIT);
        savedTask.setToUser(user);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        Task result = taskService.create(requestDto, principal);

        // Assert
        assertNotNull(result);
        assertEquals("New task", result.getDescription());
        verify(taskRepository).save(any(Task.class));
        verify(userService).getByEmail("test@example.com");
    }

    @Test
    void deleteById_ShouldCallRepository() {
        // Arrange
        Task task = createTestTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(any(Task.class));

        // Act
        taskService.deleteById(1L);

        // Assert
        verify(taskRepository).delete(any(Task.class));
    }

    @Test
    void takeTask_ShouldUpdateTaskStatus() {
        // Arrange
        Task task = createTestTask();
        task.setToUser(null); // Явно убираем назначенного пользователя

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(principal.getName()).thenReturn("test@example.com");
        when(userService.getByEmail(anyString())).thenReturn(user);

        // Act
        Task result = taskService.takeTask(1L, principal);

        // Assert
        assertNotNull(result);
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        assertEquals(user, result.getToUser());
        verify(taskRepository).save(task);
        verify(userService).getByEmail("test@example.com");
    }

    @Test
    void finishTask_ShouldUpdateTaskStatus() {
        // Arrange
        Task task = createTestTask();
        task.setStatus(TaskStatus.IN_PROGRESS);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(principal.getName()).thenReturn("test@example.com");
        when(userService.getByEmail(anyString())).thenReturn(user);

        // Act
        Task result = taskService.finishTask(1L, principal);

        // Assert
        assertNotNull(result);
        assertEquals(TaskStatus.FINISHED, result.getStatus());
        assertNotNull(result.getFinishedAt());
        verify(taskRepository).save(any(Task.class));
        verify(userService).getByEmail("test@example.com");
    }

    @Test
    void getAllWithFilter_ShouldReturnPage() {
        // Arrange
        Task task = createTestTask();
        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        List<Task> result = taskService.getAll(null, Pageable.unpaged());

        // Assert
        assertEquals(1, result.size());
        verify(taskRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}