package com.ronald.jwtrest.api.controller;

import com.ronald.jwtrest.api.dto.AuthBody;
import com.ronald.jwtrest.api.dto.RegisterUserCommand;
import com.ronald.jwtrest.config.JwtTokenProvider;
import com.ronald.jwtrest.model.User;
import com.ronald.jwtrest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtp;


    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthBody credientials) {
        try {
            String email = credientials.getEmail();
            // spring qui gere cet object
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, credientials.getPassword()));
            String token = jwtp.createToken(email, userService.findByEmail(email).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("email", email);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    /*on utilise un nv dto autre que User car le client n'as pas le droit de choisir si il est enable*/
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterUserCommand command) {
        User user = userService.findByEmail(command.getEmail());
        if (user != null) {
            throw new BadCredentialsException("User with email:" + command.getEmail() + "already used");
        } else {
            userService.createUser(command);
            Map<Object, Object> model = new HashMap<>();
            model.put("message", "User registred succesfully");
            return ok(model);
        }
    }


}
