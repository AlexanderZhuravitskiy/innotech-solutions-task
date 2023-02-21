package com.example.innotechsolutionstask.service;

import com.example.innotechsolutionstask.domain.Client;
import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.dto.ClientDto;

import java.util.List;
import java.util.Map;

public interface ClientService {

    List<ClientDto> getAllClients();

    ClientDto getClientById(Long id);

    ClientDto getClientByUsername(String username);

    void createClient(ClientDto clientDto);

    void addClientTicket(Train train,
                         ClientDto clientDto);

    void addClient(ClientDto clientDto);

    void updateClientPassword(ClientDto clientDto,
                              String password);

    void updateClientUsernameAndRole(String username,
                                     Map<String, String> form,
                                     ClientDto clientDto);

    void updateClientBalance(Integer replenishmentAmount,
                             ClientDto clientDto);

    void updateClient(ClientDto user);

    void deleteClientTicket(Train train,
                            ClientDto clientDto);

    void deleteClient(ClientDto clientDto);
}
