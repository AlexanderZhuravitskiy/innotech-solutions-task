package com.example.innotechsolutionstask.controller;

import com.example.innotechsolutionstask.domain.Client;
import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.dto.ClientDto;
import com.example.innotechsolutionstask.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/users/balance")
    public String getUserBalance(@AuthenticationPrincipal Client user,
                                 Model model) {
        ClientDto clientDto = clientService.getClientById(user.getId());
        model.addAttribute("user", clientDto);
        return "balanceReplenishment";
    }

    @PostMapping("/users/balance/add")
    public String updateUserBalance(@RequestParam Integer replenishmentAmount,
                                    @AuthenticationPrincipal Client user) {
        ClientDto clientDto = clientService.getClientById(user.getId());
        clientService.updateClientBalance(replenishmentAmount, clientDto);
        return "redirect:/users/balance";
    }

    @GetMapping("/users/tickets")
    public String getUserTicketList(@AuthenticationPrincipal Client user,
                                    Model model
    ) {
        ClientDto clientDto = clientService.getClientById(user.getId());
        model.addAttribute("trains", clientDto.getTrains());
        return "userTickets";
    }

    @PostMapping("/users/tickets/{train}/add")
    public String addTicket(@PathVariable Train train,
                            @AuthenticationPrincipal Client user) {
        ClientDto clientDto = clientService.getClientById(user.getId());
        clientService.addClientTicket(train, clientDto);
        return "redirect:/main";
    }

    @PostMapping("/users/tickets/{train}/delete")
    public String deleteTicket(@PathVariable Train train,
                               @AuthenticationPrincipal Client user) {
        ClientDto clientDto = clientService.getClientById(user.getId());
        clientService.deleteClientTicket(train, clientDto);
        return "redirect:/users/tickets";
    }
}
