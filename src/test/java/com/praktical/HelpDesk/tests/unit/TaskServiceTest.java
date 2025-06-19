package com.praktical.HelpDesk.tests.unit;

import com.praktica.HelpDesk.dto.task.TaskRequestDto;
import com.praktica.HelpDesk.entity.Task;
import com.praktica.HelpDesk.repository.TaskRepository;
import com.praktica.HelpDesk.entity.TaskStatus;
import com.praktica.HelpDesk.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    private TaskRepository taskRepository;

    @Mock
    private Principal principal;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task createTestTask() {
        return Task.builder()
                .id(1L)
                .description("Test task")
                .status(TaskStatus.WAIT)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAll_ShouldReturnTasksList() {

        Task task = createTestTask();
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));

        List<Task> result = taskService.getAll();

        assertEquals(1, result.size());
        assertEquals("Test task", result.get(0).getDescription());
    }

    @Test
    void getById_ShouldReturnTask() {

        Task task = createTestTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void create_ShouldSaveNewTask() {
        // Arrange
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setDescription("New task");  // Указываем только описание

        Task savedTask = Task.builder()
                .id(1L)
                .description("New task")
                .status(TaskStatus.WAIT)  // Используем WAIT вместо OPEN
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(principal.getName()).thenReturn("test@example.com");

        // Act
        Task result = taskService.create(requestDto, principal);

        // Assert
        assertNotNull(result);
        assertEquals("New task", result.getDescription());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void deleteById_ShouldCallRepository() {

        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteById(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void takeTask_ShouldUpdateTaskStatus() {

        Task task = createTestTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(principal.getName()).thenReturn("test@example.com");

        Task result = taskService.takeTask(1L, principal);

        assertNotNull(result);
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    void finishTask_ShouldUpdateTaskStatus() {

        Task task = createTestTask();
        task.setStatus(TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(principal.getName()).thenReturn("test@example.com");

        Task result = taskService.finishTask(1L, principal);

        assertNotNull(result);
        assertEquals(TaskStatus.FINISHED, result.getStatus());
        assertNotNull(result.getFinishedAt());
    }

    @Test
    void getAllWithFilter_ShouldReturnPage() {

        Task task = createTestTask();
        Page<Task> page = new PageImpl<>(Collections.singletonList(task));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<Task> result = taskService.getAll(null, Pageable.unpaged());

        assertEquals(1, result.size());
        assertEquals("Test task", result.get(0).getDescription());
    }
}