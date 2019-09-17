package com.ronald.jwtrest.service;

import com.ronald.jwtrest.api.dto.RegisterUserCommand;
import com.ronald.jwtrest.dao.UserDao;
import com.ronald.jwtrest.model.Role;
import com.ronald.jwtrest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    // traduire le user spring en user propre à l'application
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder bCryptPwdEncoder;

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User createUser(RegisterUserCommand command) {
        User user = new User();
        user.setPassword(bCryptPwdEncoder.encode(command.getPassword())); // encode password
        user.setEnabled(true);
        user.setRoles(new HashSet<>(command.getRoles()));
        return userDao.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username);
        if (user != null) {
            // Roles et authorities sont des termes identiques
            List<GrantedAuthority> authorities = getUserAuthorities(user.getRoles());
            return buildSpringUser(user, authorities);
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }

    private List<GrantedAuthority> getUserAuthorities(Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return authorities;
    }

    private UserDetails buildSpringUser(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
