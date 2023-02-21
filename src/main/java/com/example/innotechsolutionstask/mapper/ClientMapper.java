package com.example.innotechsolutionstask.mapper;

import com.example.innotechsolutionstask.domain.Client;
import com.example.innotechsolutionstask.dto.ClientDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDto clientToClientDto(Client client);

    Client clientDtoToClient(ClientDto clientDto);
}
