package com.praktica.HelpDesk.secutiry;

import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityService securityService;

    private final UserService userService;

    //В данном методе хардкод. Метод getEmailFromToken возвращает почему-то id пользователя
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println(header);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (securityService.validateToken(token)) {
                Long userId = Long.parseLong(securityService.getEmailFromToken(token));
                UserEntity userEntity = userService.getById(userId);
                String email = userEntity.getEmail();
                String role = userEntity.getRole().toString();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(role)));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}