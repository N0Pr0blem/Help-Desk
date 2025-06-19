package com.praktica.HelpDesk.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(toBuilder = true)
public class AuthResponseDto {
    private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;
    private String role;
}
