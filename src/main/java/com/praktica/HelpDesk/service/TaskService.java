package com.praktica.HelpDesk.service;

import com.praktica.HelpDesk.dto.filter.TaskFilter;
import com.praktica.HelpDesk.dto.task.TaskRequestDto;
import com.praktica.HelpDesk.entity.Task;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface TaskService {
    List<Task> getAll();

    List<Task> getAll(TaskFilter taskFilter, Pageable pageable);

    Task getById(Long taskId);

    Task create(TaskRequestDto taskRequestDto, Principal principal);

    void deleteById(Long id);
}
