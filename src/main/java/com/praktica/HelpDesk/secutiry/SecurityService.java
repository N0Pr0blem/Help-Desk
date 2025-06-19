package com.praktica.HelpDesk.secutiry;

public interface SecurityService {
    TokenDetails authenticate(String email, String password);

    boolean validateToken(String token);

    String getEmailFromToken(String token);
}
