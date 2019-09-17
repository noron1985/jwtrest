package com.ronald.jwtrest.config;

import com.ronald.jwtrest.model.Role;
import com.ronald.jwtrest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwttp;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService uds = userDetails();
        auth.userDetailsService(uds).passwordEncoder(bCryptPwdEncoder());
    }

    //CORS
    //CSRF Cross-site request forgery
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        // désactive l'authentification basique du protocole HTTP
        security.httpBasic().disable()
                // on désactive la sécurité qui empêche un pirate de faire des requetes depuis son ordinateur
                // inconnu de son systeme
                .csrf().disable()
                // l'architecture REST ne doit pas sauvegarder de session, sans état
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                // autorise tlm à se login et à s'enregistrer
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/api/auth/register").permitAll()
                // règle global pour avoir accès à tous les ADMIN et tous les USER au produits et sous produits
                .antMatchers(HttpMethod.GET, "/api/products/**").hasAnyAuthority(Role.ADMIN.toString(), Role.USER.toString())
                // on autorise seulement l'ADMIN à inserer des produits
                .antMatchers(HttpMethod.POST, "/api/products/**").hasAuthority(Role.ADMIN.toString())
                // on définit la réponse lorsque le lcient n'est pas autorisé
                .and().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())
                // on ajoute le filtre
                .and().addFilterBefore(new JwtTokenFilter(jwttp), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        // Classe fonctionnelle qui n'integre que une methode
//        return new AuthenticationEntryPoint() {
//            @Override
//            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//
//            }
//        };
        return ((httpServletRequest, httpServletResponse, authException) -> httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));
    }

    @Bean
    public PasswordEncoder bCryptPwdEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetails() {
        return new UserService();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
