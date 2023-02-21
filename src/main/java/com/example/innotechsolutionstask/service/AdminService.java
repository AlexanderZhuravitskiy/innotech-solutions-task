package com.example.innotechsolutionstask.service;

import com.example.innotechsolutionstask.dto.AdminDto;

import java.util.List;

public interface AdminService {
    List<AdminDto> getAllAdmins();

    AdminDto getAdminById(Long id);

    void addAdmin(AdminDto adminDto);

    void updateAdminPassword(AdminDto adminDto, String password);

    void updateAdmin(AdminDto adminDto);

    void deleteAdmin(AdminDto adminDto);
}
