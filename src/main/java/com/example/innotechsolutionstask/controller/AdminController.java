package com.example.innotechsolutionstask.controller;

import com.example.innotechsolutionstask.domain.Client;
import com.example.innotechsolutionstask.domain.Role;
import com.example.innotechsolutionstask.dto.ClientDto;
import com.example.innotechsolutionstask.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final ClientService clientService;

    @GetMapping("/users")
    public String getClientsList(Model model) {
        List<ClientDto> userList = clientService.getAllClients();
        model.addAttribute("users", userList);
        return "userList";
    }

    @GetMapping("/users/{user}")
    public String editClient(@PathVariable Client user,
                             Model model) {
        ClientDto clientDto = clientService.getClientById(user.getId());
        model.addAttribute("user", clientDto);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("/users/{user}/update")
    public String saveClient(@PathVariable Client user,
                             @RequestParam String username,
                             @RequestParam Map<String, String> form
    ) {
        ClientDto clientDto = clientService.getClientById(user.getId());
        clientService.updateClientUsernameAndRole(username, form, clientDto);
        return "redirect:/users";
    }
}