package com.praktica.HelpDesk.secutiry;

import com.praktica.HelpDesk.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Base64;
import java.util.Date;

public class JwtHandler {
    private final String secret;

    public JwtHandler(String secret) {
        this.secret = secret;
    }

    public VerificationResult check(String accessToken){
           return verify(accessToken);
    }

    private VerificationResult verify(String token){
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();

        if(expirationDate.before(new Date())){
            throw new ApiException("Token expired","TOKEN_EXCEPTION");
        }

        return  new VerificationResult(claims, token);
    }

    private Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static class VerificationResult{
        public Claims claims;
        public String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}
