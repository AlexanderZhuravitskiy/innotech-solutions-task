package com.example.innotechsolutionstask.repos;

import com.example.innotechsolutionstask.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    Client findByUsername(String username);
}