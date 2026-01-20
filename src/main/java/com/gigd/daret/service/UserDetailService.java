package com.gigd.daret.service;

import java.util.Collections;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gigd.daret.models.User;
import com.gigd.daret.repository.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userRepo.findByEmail(authentication.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getMot_de_passe(),
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())));
    }
    
    
    
}