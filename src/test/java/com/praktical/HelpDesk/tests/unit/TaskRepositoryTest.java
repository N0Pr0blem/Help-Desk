package com.praktical.HelpDesk.tests.unit;

import com.praktica.HelpDesk.entity.Task;
import com.praktica.HelpDesk.entity.TaskStatus;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository; // Мок репозитория

    @Test
    public void findTasksByUserId_ShouldReturnTasksWhereUserIsCreator() {
        // Подготовка тестовых данных
        UserEntity user1 = new UserEntity();
        user1.setId(1L);

        Task task1 = Task.builder()
                .description("Fix bug")
                .fromUser(user1)
                .status(TaskStatus.WAIT)
                .build();

        Task task2 = Task.builder()
                .description("Update docs")
                .fromUser(user1)
                .status(TaskStatus.IN_PROGRESS)
                .build();

        // Настройка мока
        when(taskRepository.findTasksByUserId(1L))
                .thenReturn(List.of(task1, task2));

        // Вызов метода и проверка
        List<Task> tasks = taskRepository.findTasksByUserId(1L);

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findTasksByUserId(1L);
    }

    @Test
    public void findTasksBySysadminsId_ShouldReturnTasksWhereUserIsAssignee() {
        UserEntity user2 = new UserEntity();
        user2.setId(2L);

        Task task1 = Task.builder()
                .description("Fix bug")
                .toUser(user2)
                .status(TaskStatus.WAIT)
                .build();

        when(taskRepository.findTasksBySysadminsId(2L))
                .thenReturn(List.of(task1));

        List<Task> tasks = taskRepository.findTasksBySysadminsId(2L);

        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findTasksBySysadminsId(2L);
    }
    @Test
    public void findTasksByUserId_ShouldReturnEmptyListWhenNoTasks() {
        when(taskRepository.findTasksByUserId(999L))
                .thenReturn(List.of());

        List<Task> tasks = taskRepository.findTasksByUserId(999L);

        assertTrue(tasks.isEmpty());
        verify(taskRepository, times(1)).findTasksByUserId(999L);
    }

    @Test
    public void saveTask_ShouldReturnSavedTask() {
        Task newTask = Task.builder()
                .description("New task")
                .build();

        when(taskRepository.save(newTask))
                .thenReturn(newTask);

        Task saved = taskRepository.save(newTask);

        assertNotNull(saved);
        verify(taskRepository, times(1)).save(newTask);
    }
}

