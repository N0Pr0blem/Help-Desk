package com.praktica.HelpDesk.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String secondName;
    private String lastName;
    private String role;
    private boolean isActive;
}
