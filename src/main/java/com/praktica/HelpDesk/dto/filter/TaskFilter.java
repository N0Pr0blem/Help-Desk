package com.praktica.HelpDesk.dto.filter;

import com.praktica.HelpDesk.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TaskFilter {
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private TaskStatus status;
}
