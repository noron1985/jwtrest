package com.ronald.jwtrest.config;

import com.ronald.jwtrest.model.Role;
import com.ronald.jwtrest.service.UserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private String secret = "secret";
    private long validityMs = 3600000; //1H

    @Autowired
    private UserService cuds;

    @PostConstruct
    private void encodeSecret() {
        // tableau vide initialisation
        byte[] values = new byte[124];

        new SecureRandom().nextBytes(values);
        // passage par référence
        secret = Base64.getEncoder().encodeToString(values);
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

    /*extraire le token du header de la requete*/
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        /*jusqu ici le token ressemble à Bearer sldfsflsmldfdkggkoskfsl*/
        /*bearer est un mot de covention devant tous les tokens jwt*/
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());//on supprime le bearer devant le token et on renvoit le token
        }
        logger.info("Wrong token : " + bearerToken);
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());// si le token est avant la date cela veut dire qu'il est expiré
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new JwtException("Invalid JWT token"); //ne va pas retourner false ou true car c'est la ligne 54qui s'en charge
        }
    }

    /*Creer un objet Authentification qui sera plus tard vérifié par Spring*/
    Authentication getAuthentification(String token) {
        UserDetails userDetails = cuds.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /*Extraire la propriété email de la partie payload(claims) du token*/
    // propriété sub de payload
    private String getEmail(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }



}
