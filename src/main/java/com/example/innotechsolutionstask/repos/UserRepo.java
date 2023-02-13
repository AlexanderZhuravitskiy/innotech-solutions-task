package com.example.innotechsolutionstask.repos;

import com.example.innotechsolutionstask.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
