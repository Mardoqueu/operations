package com.challenge.operations.service;

import com.challenge.operations.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * CustomUserDetailsService is an implementation of the UserDetailsService interface,
 * which is used for loading user-specific data during authentication.
 *
 * This class primarily interacts with the UserService to retrieve user details
 * based on the username and prepares the UserDetails object required by
 * Spring Security for authentication and authorization purposes.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * This field is a Spring-managed bean of type UserService that provides
     * various user management functionalities such as finding users by
     * username, updating user information, and managing user balances.
     * It is automatically injected by the Spring framework.
     */
    @Autowired
    private UserService userService;

    /**
     * Loads the user details for the given username.
     *
     * @param username the username of the user whose details are to be loaded
     * @return the UserDetails object containing user information such as username, password, and roles
     * @throws UsernameNotFoundException if the user with the given username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
