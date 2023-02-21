package com.example.innotechsolutionstask.service.implementation;

import com.example.innotechsolutionstask.domain.Admin;

import com.example.innotechsolutionstask.dto.AdminDto;
import com.example.innotechsolutionstask.exceptions.NotFoundException;
import com.example.innotechsolutionstask.mapper.AdminMapper;
import com.example.innotechsolutionstask.repos.AdminRepo;
import com.example.innotechsolutionstask.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepo adminRepo;

    private final PasswordEncoder passwordEncoder;

    private final AdminMapper adminMapper;

    @Override
    public List<AdminDto> getAllAdmins() {
        log.info("Fetching a list of admins");
        List<Admin> adminList = adminRepo.findAll();
        log.info("Received a list of admins: {}", adminList);
        return adminList.stream()
                .filter(Objects::nonNull)
                .map(adminMapper::adminToAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdminDto getAdminById(Long id) {
        log.info("Fetching a admin by id: {}", id);
        Admin admin = adminRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin with id: " + id + " not found"));
        log.info("Received admin: {}", admin);
        return adminMapper.adminToAdminDto(admin);
    }

    @Override
    @Transactional
    public void addAdmin(AdminDto adminDto) {
        Admin admin = adminMapper.adminDtoToAdmin(adminDto);
        adminRepo.save(admin);
        log.info("Saving admin: {}", admin);
    }

    @Override
    @Transactional
    public void updateAdminPassword(AdminDto adminDto, String password) {
        if (!password.isBlank()) {
            Admin admin = adminMapper.adminDtoToAdmin(adminDto);
            admin.setPassword(passwordEncoder.encode(password));
            adminRepo.save(admin);
            log.info("Saving admin: {}", admin);
        }
    }

    @Override
    @Transactional
    public void updateAdmin(AdminDto adminDto) {
        Admin admin = adminMapper.adminDtoToAdmin(adminDto);
        adminRepo.save(admin);
        log.info("Saving update admin: {}", admin);
    }

    @Override
    @Transactional
    public void deleteAdmin(AdminDto adminDto) {
        Admin admin = adminMapper.adminDtoToAdmin(adminDto);
        adminRepo.delete(admin);
        log.info("Deleting admin: {}", admin);
    }
}
