package com.praktica.HelpDesk.controller;

import com.praktica.HelpDesk.dto.task.TaskRequestDto;
import com.praktica.HelpDesk.dto.task.TaskResponseDto;
import com.praktica.HelpDesk.mapper.TaskMapper;
import com.praktica.HelpDesk.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
@PreAuthorize("isAuthenticated()")
public class BaseTaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto, Principal principal) {
        return ResponseEntity.ok(taskMapper.toDto(taskService.create(taskRequestDto,principal)));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> myTasks(Principal principal){
        return ResponseEntity.ok(taskMapper.toDtos(taskService.getUsersTasks(principal)));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable("taskId") Long taskId,Principal principal){
        return ResponseEntity.ok(taskMapper.toDto(taskService.getById(taskId, principal)));
    }
}
