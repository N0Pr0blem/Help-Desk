package com.praktica.HelpDesk.dto.task;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.praktica.HelpDesk.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskResponseDto {
    private Long id;
    private String description;
    private Long fromUserId;
    private Long toUserId;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private TaskStatus status;
}
