package com.praktica.HelpDesk.secutiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TokenDetails {
    private Long id;
    private String token;
    private Date issuedAt;
    private Date expiredAt;
    private String role;
}
