package com.ronald.jwtrest.api.dto;

import com.ronald.jwtrest.model.Role;

import java.util.Set;

public class RegisterUserCommand {

    private String email;
    private String password;
    private String fullname;
    private Set<Role> roles;


    protected RegisterUserCommand() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }


    public Set<Role> getRoles() {
        return roles;
    }
}
