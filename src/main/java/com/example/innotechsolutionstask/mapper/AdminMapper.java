package com.example.innotechsolutionstask.mapper;

import com.example.innotechsolutionstask.domain.Admin;
import com.example.innotechsolutionstask.dto.AdminDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDto adminToAdminDto(Admin admin);

    Admin adminDtoToAdmin(AdminDto adminDto);
}
