package com.praktica.HelpDesk.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRequestDto {
    @Email
    private String email;

    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    private String password;

    @Size(min = 2, max = 64, message = "FirstName must be between 2 and 64 characters")
    private String firstName;

    @Size(min = 2, max = 64, message = "SecondName must be between 2 and 64 characters")
    private String secondName;

    @Size(min = 2, max = 64, message = "LastName must be between 2 and 64 characters")
    private String lastName;

    private String role;
}
