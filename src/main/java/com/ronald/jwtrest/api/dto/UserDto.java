package com.ronald.jwtrest.api.dto;

import com.ronald.jwtrest.model.Role;

import java.util.Set;

public class UserDto {

    private String email;
    private String password;
    private String fullname;
    private boolean enabled;
    private Set<Role> roles;

    public UserDto(String email, String password, String fullname, boolean enabled, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
