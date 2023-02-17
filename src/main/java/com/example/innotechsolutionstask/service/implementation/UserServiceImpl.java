package com.example.innotechsolutionstask.service.implementation;

import com.example.innotechsolutionstask.domain.User;
import com.example.innotechsolutionstask.repos.UserRepo;
import com.example.innotechsolutionstask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService, UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Fetching a admin by username: {}", username);
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }
        return user;
    }
}
