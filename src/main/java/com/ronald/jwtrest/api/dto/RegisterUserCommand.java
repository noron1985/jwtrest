package com.ronald.jwtrest.api.dto;

import com.ronald.jwtrest.model.Role;

import java.util.Set;

public class RegisterUserCommand {

    private String email;
    private String password;
    private String fullname;
    private Set<Role> roles;

    public RegisterUserCommand(String email, String password, String fullname, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.roles = roles;
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
