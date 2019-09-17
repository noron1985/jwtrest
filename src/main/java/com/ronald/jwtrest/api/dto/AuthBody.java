package com.ronald.jwtrest.api.dto;

public class AuthBody {

    private String email;
    private String password;

    protected AuthBody() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
