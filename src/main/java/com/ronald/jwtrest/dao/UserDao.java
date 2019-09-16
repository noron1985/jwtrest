package com.ronald.jwtrest.dao;

import com.ronald.jwtrest.model.User;

public interface UserDao {

    User findByEmail(String email);

    User save (User user);

}
