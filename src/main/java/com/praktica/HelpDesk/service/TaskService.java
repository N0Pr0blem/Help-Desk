package com.praktica.HelpDesk.service;

import com.praktica.HelpDesk.dto.filter.TaskFilter;
import com.praktica.HelpDesk.dto.task.TaskRequestDto;
import com.praktica.HelpDesk.dto.task.TaskResponseDto;
import com.praktica.HelpDesk.entity.Task;
import com.praktica.HelpDesk.entity.TaskStatus;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface TaskService {
    List<Task> getAll();

    List<Task> getAll(TaskFilter taskFilter, Pageable pageable);

    Task getById(Long taskId);

    Task getById(Long taskId, Principal principal);

    Task create(TaskRequestDto taskRequestDto, Principal principal);

    void deleteById(Long id);

    List<Task> getUsersTasks(Principal principal);

    List<Task> getSysadminsTasks(Principal principal);

    Task takeTask(Long taskId,Principal principal);

    Task finishTask(Long taskId,Principal principal);

    List<Task> getSysadminsTasks(TaskStatus taskStatus, Principal principal);
}
