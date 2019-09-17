package com.ronald.jwtrest.dao.impl;

import com.ronald.jwtrest.dao.UserDao;
import com.ronald.jwtrest.model.User;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {


    List<User> users = new ArrayList<>();

    @Override
    public User findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public User save(User user) {
        //avec un index c'est plus robuste car la methode remove utilise l'equal user donc va remplacer à la fin de la liste le nouvelle utilisateur

        if (findByEmail(user.getEmail()) == null){
            users.add(user);
        } else {
            users.remove(user);
            users.add(user);
        }

        return findByEmail(user.getEmail());

    }


    @PostConstruct
    private void onPostConstruct(){

    }
}
