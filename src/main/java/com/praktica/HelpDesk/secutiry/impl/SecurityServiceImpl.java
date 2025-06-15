package com.praktica.HelpDesk.secutiry.impl;

import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.exception.AuthException;
import com.praktica.HelpDesk.secutiry.SecurityService;
import com.praktica.HelpDesk.secutiry.TokenDetails;
import com.praktica.HelpDesk.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    @Override
    public TokenDetails authenticate(String email, String password) {
       UserEntity user = userService.getByEmail(email);
        if (!user.isActive()) {
            throw new AuthException("Account disabled", "ACCOUNT_DISABLED");
        }
        if (!user.getPassword().equals(passwordEncoder.encode(password))) {
            throw new AuthException("Invalid password", "INVALID_PASSWORD");
        }

        return generateToken(user).toBuilder()
                .id(user.getId())
                .role(user.getRole().toString())
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);


            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Token expired: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }

    private TokenDetails generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("email", user.getEmail());
        }};

        return generateToken(claims, user.getId().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return new TokenDetails().toBuilder()
                .token(token)
                .expiredAt(expirationDate)
                .issuedAt(createdDate)
                .build();
    }

    public String getEmailFromToken(String token) {
        String email =  Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        System.out.println(email);

        return email;
    }
}
