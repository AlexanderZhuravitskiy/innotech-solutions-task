package com.example.innotechsolutionstask.service.implementation;

import com.example.innotechsolutionstask.domain.Admin;
import com.example.innotechsolutionstask.domain.Client;
import com.example.innotechsolutionstask.domain.Role;
import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.dto.ClientDto;
import com.example.innotechsolutionstask.exceptions.NotFoundException;
import com.example.innotechsolutionstask.mapper.ClientMapper;
import com.example.innotechsolutionstask.repos.AdminRepo;
import com.example.innotechsolutionstask.repos.ClientRepo;
import com.example.innotechsolutionstask.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepo clientRepo;

    private final AdminRepo adminRepo;

    private final PasswordEncoder passwordEncoder;

    private final ClientMapper clientMapper;

    @Override
    public List<ClientDto> getAllClients() {
        log.info("Fetching a list of clients");
        List<Client> clientList = clientRepo.findAll();
        log.info("Received a list of clients: {}", clientList);
        return clientList.stream()
                .filter(Objects::nonNull)
                .map(clientMapper::clientToClientDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto getClientById(Long id) {
        log.info("Fetching a client by id: {}", id);
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Client with id: " + id + " not found"));
        log.info("Received client: {}", client);
        return clientMapper.clientToClientDto(client);
    }

    @Override
    public ClientDto getClientByUsername(String username) {
        log.info("Fetching a user by username: {}", username);
        Client client = clientRepo.findByUsername(username);
        log.info("Received client: {}", client);
        return clientMapper.clientToClientDto(client);
    }

    @Override
    @Transactional
    public void createClient(ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setActive(true);
        client.setBalance(0);
        client.setRole(Role.USER);
        clientRepo.save(client);
        log.info("Saving client: {}", client);
    }

    @Override
    @Transactional
    public void addClientTicket(Train train, ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        if (train.getFreePlaces() > 0 && train.getPrice() <= client.getBalance()) {
            train.setFreePlaces(train.getFreePlaces() - 1);
            client.setBalance(client.getBalance() - train.getPrice());
            client.getTrains().add(train);
            clientRepo.save(client);
            log.info("Saving update client: {}", client);
        }
    }

    @Override
    @Transactional
    public void addClient(ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        clientRepo.save(client);
        log.info("Saving client: {}", client);
    }

    @Override
    @Transactional
    public void updateClientPassword(ClientDto clientDto, String password) {
        if (!password.isBlank()) {
            Client client = clientMapper.clientDtoToClient(clientDto);
            client.setPassword(passwordEncoder.encode(password));
            clientRepo.save(client);
            log.info("Saving updated client: {}", client);
        }
    }

    @Override
    @Transactional
    public void updateClientUsernameAndRole(String username, Map<String, String> form, ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        client.setUsername(username);
        boolean isRoleChanged = false;
        for (String key : form.keySet()) {
            if (key.equals("checkbox")) {
                isRoleChanged = true;
                break;
            }
        }
        if(isRoleChanged) {
            Admin admin = new Admin(client.getUsername(), client.getPassword(), client.isActive(), Role.ADMIN);
            adminRepo.save(admin);
            clientRepo.delete(client);
            log.info("Deleting client: {}", client);
        } else {
            clientRepo.save(client);
            log.info("Saving update client: {}", client);
        }
    }

    @Override
    @Transactional
    public void updateClientBalance(Integer replenishmentAmount, ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        client.setBalance(client.getBalance() + replenishmentAmount);
        clientRepo.save(client);
        log.info("Saving update client: {}", client);
    }

    @Override
    @Transactional
    public void updateClient(ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        clientRepo.save(client);
        log.info("Saving update client: {}", client);
    }

    @Override
    @Transactional
    public void deleteClientTicket(Train train, ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        client.setBalance(client.getBalance() + train.getPrice());
        client.getTrains().remove(train);
        train.setFreePlaces(train.getFreePlaces() + 1);
        clientRepo.save(client);
        log.info("Saving update client: {}", client);
    }

    @Override
    @Transactional
    public void deleteClient(ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        clientRepo.delete(client);
        log.info("Deleting client: {}", client);
    }
}
