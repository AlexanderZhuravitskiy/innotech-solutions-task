package com.example.innotechsolutionstask.service;

import com.example.innotechsolutionstask.domain.Client;
import com.example.innotechsolutionstask.domain.Train;

import java.util.List;
import java.util.Map;

public interface ClientService {

    List<Client> getAllClients();

    Client getClientById(Long id);

    Client getClientByUsername(String username);

    void createClient(Client user);

    void addClientTicket(Train train,
                         Client user);

    void addClient(Client user);

    void updateClientPassword(Client user,
                              String password);

    void updateClientUsernameAndRole(String username,
                                     Map<String, String> form,
                                     Client user);

    void updateClientBalance(Integer replenishmentAmount,
                             Client user);

    void updateClient(Client user);

    void deleteClientTicket(Train train,
                            Client user);

    void deleteClient(Client user);
}
