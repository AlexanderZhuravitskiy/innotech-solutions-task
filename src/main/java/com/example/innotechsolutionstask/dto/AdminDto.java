package com.example.innotechsolutionstask.dto;

import com.example.innotechsolutionstask.domain.Role;
import lombok.Data;

@Data
public class AdminDto {
    private Long id;

    private String username;

    private String password;

    private boolean active;

    private Role role;
}
