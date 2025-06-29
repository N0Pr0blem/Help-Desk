package com.praktica.HelpDesk.controller;

import com.praktica.HelpDesk.mapper.TaskMapper;
import com.praktica.HelpDesk.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminTaskController {

    private final TaskService taskService;

    @PostMapping("/set/{taskId}/to/{userId}")
    public ResponseEntity<String> setTaskToSysadmin(@PathVariable("taskId") Long taskId, @PathVariable("userId") Long userId) {
        taskService.setTaskToSysadmin(taskId, userId);
        return ResponseEntity.ok("Task successfully set");
    }
}
