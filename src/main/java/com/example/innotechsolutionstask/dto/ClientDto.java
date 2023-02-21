package com.example.innotechsolutionstask.dto;

import com.example.innotechsolutionstask.domain.Role;
import com.example.innotechsolutionstask.domain.Train;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClientDto {
    private Long id;

    private String username;

    private String password;

    private boolean active;

    private Role role;

    private Integer balance;

    private List<Train> trains = new ArrayList<>();
}
