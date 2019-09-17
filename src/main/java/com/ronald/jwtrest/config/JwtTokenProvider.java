package com.ronald.jwtrest.config;

import com.ronald.jwtrest.model.Role;
import com.ronald.jwtrest.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private String secret = "secret";
    private long validityMs = 3600000; //1H

    private UserService cuds;

    private void encodeSecret() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String email, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMs);
        return Jwts.builder()
                .setClaims(claims) // claims = payload
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

}
