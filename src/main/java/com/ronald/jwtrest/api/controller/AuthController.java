package com.ronald.jwtrest.api.controller;

import com.ronald.jwtrest.api.dto.RegisterUserCommand;
import com.ronald.jwtrest.config.JwtTokenProvider;
import com.ronald.jwtrest.dao.UserDao;
import com.ronald.jwtrest.model.User;
import com.ronald.jwtrest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtp;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @PostMapping("/registry")
    public ResponseEntity register(@RequestBody RegisterUserCommand command) {
        User user = userService.findByEmail(command.getEmail());
        if (user != null) {
            throw new BadCredentialsException("User with email:" + command.getEmail() + "already used");
        } else {
            userService.createUser(command);
            Map<Object, Object> model = new HashMap<>();
            model.put("message", "User registred succesfully");
            return ResponseEntity.ok(model);
        }
    }
}
