package com.praktica.HelpDesk.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequestDto {
    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    private String password;

    @NotNull
    @Size(min = 2, max = 64, message = "FirstName must be between 2 and 64 characters")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 64, message = "SecondName must be between 2 and 64 characters")
    private String secondName;

    @NotNull
    @Size(min = 2, max = 64, message = "LastName must be between 2 and 64 characters")
    private String lastName;
}
