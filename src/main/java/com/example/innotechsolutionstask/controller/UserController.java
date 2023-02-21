package com.example.innotechsolutionstask.controller;

import com.example.innotechsolutionstask.domain.Admin;
import com.example.innotechsolutionstask.domain.Client;
import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.domain.User;
import com.example.innotechsolutionstask.dto.AdminDto;
import com.example.innotechsolutionstask.dto.CaptchaResponseDto;
import com.example.innotechsolutionstask.dto.ClientDto;
import com.example.innotechsolutionstask.mapper.ClientMapper;
import com.example.innotechsolutionstask.service.AdminService;
import com.example.innotechsolutionstask.service.ClientService;
import com.example.innotechsolutionstask.web.constant.WebConstant;
import com.example.innotechsolutionstask.web.handler.ControllerExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

import static com.example.innotechsolutionstask.web.constant.WebConstant.CAPTCHA_URL;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final ClientService clientService;

    private final AdminService adminService;

    private final RestTemplate restTemplate;

    private final ClientMapper clientMapper;

    @Value("${recaptcha.secret}")
    private String secret;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }


    @PostMapping("/registration")
    public String addUser(@RequestParam String passwordRepeat,
                          @RequestParam("g-recaptcha-response") String captchaResponse,
                          @Valid Client user,
                          BindingResult bindingResult,
                          Model model) {
        ClientDto clientDto = clientMapper.clientToClientDto(user);
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (response != null && !response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }
        boolean isConfirmEmpty = passwordRepeat.isBlank();
        if (isConfirmEmpty) {
            model.addAttribute("passwordRepeatError", WebConstant.BLANK_FIELD_MESSAGE);
        }
        if (clientDto.getPassword() != null && !clientDto.getPassword().equals(passwordRepeat)) {
            model.addAttribute("passwordError", "Passwords are different!");
            return "registration";
        }
        if (response != null && (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess())) {
            Map<String, String> errors = ControllerExceptionHandler.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if (clientService.getClientByUsername(clientDto.getUsername()) != null) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        clientService.createClient(clientDto);
        return "login";
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

    @GetMapping("/users/profile")
    public String getUser(@AuthenticationPrincipal User user,
                          Model model) {
        if(user.getClass() == Admin.class) {
            AdminDto adminDto = adminService.getAdminById(user.getId());
            model.addAttribute("username", adminDto.getUsername());
        } else {
            ClientDto clientDto = clientService.getClientById(user.getId());
            model.addAttribute("username", clientDto.getUsername());
        }
        return "profile";
    }

    @PostMapping("/users/profile")
    public String updateUser(@AuthenticationPrincipal User user,
                             @RequestParam String password
    ) {
        if(user.getClass() == Admin.class) {
            AdminDto adminDto = adminService.getAdminById(user.getId());
            adminService.updateAdminPassword(adminDto, password);
        } else {
            ClientDto clientDto = clientService.getClientById(user.getId());
            clientService.updateClientPassword(clientDto, password);
        }
        return "redirect:/users/profile";
    }
}