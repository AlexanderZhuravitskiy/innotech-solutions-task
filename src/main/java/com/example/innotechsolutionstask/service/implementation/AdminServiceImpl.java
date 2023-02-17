package com.example.innotechsolutionstask.service.implementation;

import com.example.innotechsolutionstask.domain.Admin;

import com.example.innotechsolutionstask.exceptions.NotFoundException;
import com.example.innotechsolutionstask.repos.AdminRepo;
import com.example.innotechsolutionstask.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepo adminRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Admin> getAllAdmins() {
        log.info("Fetching a list of users");
        return adminRepo.findAll();
    }

    @Override
    public Admin getAdminById(Long id) {
        log.info("Fetching a user by id: {}", id);
        return adminRepo.findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }

    @Override
    public void addAdmin(Admin user) {
        log.info("Saving user: {}", user);
        adminRepo.save(user);
    }

    @Override
    @Transactional
    public void updateAdminPassword(Admin user, String password) {
        if (!password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
            updateAdmin(user);
        }
    }

    @Override
    public void updateAdmin(Admin user) {
        log.info("Saving update user: {}", user);
        adminRepo.save(user);
    }

    @Override
    public void deleteAdmin(Admin user) {
        log.info("Deleting user: {}", user);
        adminRepo.delete(user);
    }
}
