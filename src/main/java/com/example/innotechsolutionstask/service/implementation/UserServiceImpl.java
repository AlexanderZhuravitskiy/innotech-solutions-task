package com.example.innotechsolutionstask.service.implementation;

import com.example.innotechsolutionstask.domain.Role;
import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.domain.User;
import com.example.innotechsolutionstask.exceptions.NotFoundException;
import com.example.innotechsolutionstask.repos.UserRepo;
import com.example.innotechsolutionstask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching a list of users");
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        log.info("Fetching a user by id: {}", id);
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("Fetching a user by username: {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    @Transactional
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setBalance(0);
        user.setRoles(Collections.singleton(Role.USER));
        addUser(user);
    }

    @Override
    @Transactional
    public void addUserTickets(Train train, User user) {
        if (train.getFreePlaces() > 0 && train.getPrice() <= user.getBalance()) {
            train.setFreePlaces(train.getFreePlaces() - 1);
            user.setBalance(user.getBalance() - train.getPrice());
            user.getTrains().add(train);
            updateUser(user);
        }
    }

    @Override
    public void addUser(User user) {
        log.info("Saving user: {}", user);
        userRepo.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(User user, String password) {
        if (!password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
            updateUser(user);
        }
    }

    @Override
    @Transactional
    public void updateUserPasswordAndRoles(String username, Map<String, String> form, User user) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        updateUser(user);
    }

    @Override
    @Transactional
    public void updateUserBalance(Integer replenishmentAmount, User user) {
        user.setBalance(user.getBalance() + replenishmentAmount);
        updateUser(user);
    }

    @Override
    public void updateUser(User user) {
        log.info("Saving update user: {}", user);
        userRepo.save(user);
    }

    @Override
    @Transactional
    public void deleteUserTickets(Train train, User user) {
        user.setBalance(user.getBalance() + train.getPrice());
        user.getTrains().remove(train);
        train.setFreePlaces(train.getFreePlaces() + 1);
        updateUser(user);
    }

    @Override
    public void deleteUser(User user) {
        log.info("Deleting user: {}", user);
        userRepo.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Fetching a user by username: {}", username);
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }
        return user;
    }
}
