package com.ronald.jwtrest.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwttp;

    public JwtTokenFilter(JwtTokenProvider jwttp) {
        this.jwttp = jwttp;
    }

    /*on ajoute un filtre à la chaine de filres qui précede l'acces au Handle (methode REST)

        une fois le dernier filtre passé la requete est envoyé au handler (methode REST de Controller)
    */
    // #1 etape
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwttp.resolveToken((HttpServletRequest) request);
        if (token != null && jwttp.validateToken(token)) {
            Authentication authentication = jwttp.getAuthentification(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // dans cette manière de faire qu'importe que la requete soit bonne ou non
        chain.doFilter(request, response);
    }
}
