package com.praktica.HelpDesk.controller;

import com.praktica.HelpDesk.dto.filter.TaskFilter;
import com.praktica.HelpDesk.dto.task.TaskRequestDto;
import com.praktica.HelpDesk.dto.task.TaskResponseDto;
import com.praktica.HelpDesk.entity.TaskStatus;
import com.praktica.HelpDesk.mapper.TaskMapper;
import com.praktica.HelpDesk.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(
            @RequestParam(required = false) LocalDateTime createdAt,
            @RequestParam(required = false) LocalDateTime finishedAt,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort
            ) {

        TaskFilter taskFilter = new TaskFilter(createdAt,finishedAt,status);
        Pageable pageable = PageRequest.of(page,size, parseSort(sort));
        List<TaskResponseDto> tasks = taskMapper.toDtos(taskService.getAll(taskFilter, pageable));

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable("taskId") Long taskId){
        return ResponseEntity.ok(taskMapper.toDto(taskService.getById(taskId)));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto taskRequestDto, Principal principal) {
        return ResponseEntity.ok(taskMapper.toDto(taskService.create(taskRequestDto, principal)));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> delete(@PathVariable("taskId") Long taskId){
        taskService.deleteById(taskId);
        return ResponseEntity.ok("Task successfully delete");
    }

    private Sort parseSort(String[] sort) {
        if (sort.length >= 2) {
            String property = sort[0];
            String direction = sort[1];
            return Sort.by(Sort.Direction.fromString(direction), property);
        }
        return Sort.unsorted();
    }
}
