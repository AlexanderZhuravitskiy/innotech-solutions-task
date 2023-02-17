package com.example.innotechsolutionstask.service;

import com.example.innotechsolutionstask.domain.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmins();

    Admin getAdminById(Long id);

    void addAdmin(Admin user);

    void updateAdminPassword(Admin userFromDatabase, String password);

    void updateAdmin(Admin user);

    void deleteAdmin(Admin user);
}
