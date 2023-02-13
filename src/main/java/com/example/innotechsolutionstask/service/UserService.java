package com.example.innotechsolutionstask.service;

import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    void createUser(User user);

    void addUserTickets(Train train,
                        User user);

    void addUser(User user);

    void updatePassword(User user,
                        String password);

    void updateUserPasswordAndRoles(String username,
                                    Map<String, String> form,
                                    User user);

    void updateUserBalance(Integer replenishmentAmount,
                           User user);

    void updateUser(User user);

    void deleteUserTickets(Train train,
                           User user);

    void deleteUser(User user);
}
