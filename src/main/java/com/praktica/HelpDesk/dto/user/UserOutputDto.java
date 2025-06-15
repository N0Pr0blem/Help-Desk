package com.praktica.HelpDesk.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserOutputDto {
    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private String lastName;
    private String role;
}
