package com.praktica.HelpDesk.secutiry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;

@Data
@AllArgsConstructor
public class CustomPrincipal implements Principal {
    private Long id;
    private String email;

    @Override
    public String getName() {
        return email;
    }
}
