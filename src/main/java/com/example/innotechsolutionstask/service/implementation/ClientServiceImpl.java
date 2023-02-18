package com.example.innotechsolutionstask.service.implementation;

import com.example.innotechsolutionstask.domain.Admin;
import com.example.innotechsolutionstask.domain.Client;
import com.example.innotechsolutionstask.domain.Role;
import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.exceptions.NotFoundException;
import com.example.innotechsolutionstask.repos.ClientRepo;
import com.example.innotechsolutionstask.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepo clientRepo;

    private final AdminServiceImpl adminService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Client> getAllClients() {
        log.info("Fetching a list of users");
        return clientRepo.findAll();
    }

    @Override
    public Client getClientById(Long id) {
        log.info("Fetching a user by id: {}", id);
        return clientRepo.findById(id).orElseThrow(() -> new NotFoundException("Client with id: " + id + " not found"));
    }

    @Override
    public Client getClientByUsername(String username) {
        log.info("Fetching a user by username: {}", username);
        return clientRepo.findByUsername(username);
    }

    @Override
    @Transactional
    public void createClient(Client user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setBalance(0);
        user.setRole(Role.USER);
        addClient(user);
    }

    @Override
    @Transactional
    public void addClientTicket(Train train, Client user) {
        if (train.getFreePlaces() > 0 && train.getPrice() <= user.getBalance()) {
            train.setFreePlaces(train.getFreePlaces() - 1);
            user.setBalance(user.getBalance() - train.getPrice());
            user.getTrains().add(train);
            updateClient(user);
        }
    }

    @Override
    public void addClient(Client user) {
        log.info("Saving user: {}", user);
        clientRepo.save(user);
    }

    @Override
    @Transactional
    public void updateClientPassword(Client user, String password) {
        if (!password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
            updateClient(user);
        }
    }

    @Override
    @Transactional
    public void updateClientUsernameAndRole(String username, Map<String, String> form, Client user) {
        user.setUsername(username);
        boolean isRoleChanged = false;
        for (String key : form.keySet()) {
            if (key.equals("checkbox")) {
                isRoleChanged = true;
                break;
            }
        }
        if(isRoleChanged) {
            Admin admin = new Admin(user.getUsername(), user.getPassword(), user.isActive(), Role.ADMIN);
            adminService.addAdmin(admin);
            deleteClient(user);
        } else {
            updateClient(user);
        }
    }

    @Override
    @Transactional
    public void updateClientBalance(Integer replenishmentAmount, Client user) {
        user.setBalance(user.getBalance() + replenishmentAmount);
        updateClient(user);
    }

    @Override
    public void updateClient(Client user) {
        log.info("Saving update user: {}", user);
        clientRepo.save(user);
    }

    @Override
    @Transactional
    public void deleteClientTicket(Train train, Client user) {
        user.setBalance(user.getBalance() + train.getPrice());
        user.getTrains().remove(train);
        train.setFreePlaces(train.getFreePlaces() + 1);
        updateClient(user);
    }

    @Override
    public void deleteClient(Client user) {
        log.info("Deleting user: {}", user);
        clientRepo.delete(user);
    }
}
