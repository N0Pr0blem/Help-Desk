package com.praktica.HelpDesk.secutiry;

import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.exception.AuthException;
import com.praktica.HelpDesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        UserEntity userEntity = userService.getById(principal.getId());
        System.out.println(principal.getName());
        if (!userEntity.isActive()) {
            throw new AuthException("This user hasn't been activated", "INACTIVE_USER");
        }

        return authentication;
    }
}
