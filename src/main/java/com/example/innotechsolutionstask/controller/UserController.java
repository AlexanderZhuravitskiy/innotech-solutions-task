package com.example.innotechsolutionstask.controller;

import com.example.innotechsolutionstask.domain.Admin;
import com.example.innotechsolutionstask.domain.Client;
import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.domain.User;
import com.example.innotechsolutionstask.dto.CaptchaResponseDto;
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
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (response != null && !response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }
        boolean isConfirmEmpty = passwordRepeat.isBlank();
        if (isConfirmEmpty) {
            model.addAttribute("passwordRepeatError", WebConstant.BLANK_FIELD_MESSAGE);
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordRepeat)) {
            model.addAttribute("passwordError", "Passwords are different!");
            return "registration";
        }
        if (response != null && (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess())) {
            Map<String, String> errors = ControllerExceptionHandler.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if (clientService.getClientByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        clientService.createClient(user);
        return "login";
    }

    @GetMapping("/users/tickets")
    public String getUserTicketList(@AuthenticationPrincipal Client user,
                                    Model model
    ) {
        Client userFromDatabase = clientService.getClientById(user.getId());
        model.addAttribute("trains", userFromDatabase.getTrains());
        return "userTickets";
    }

    @PostMapping("/users/tickets/add")
    public String buyTicket(@RequestParam("trainId") Train train,
                            @AuthenticationPrincipal Client user) {
        Client userFromDatabase = clientService.getClientById(user.getId());
        clientService.addClientTicket(train, userFromDatabase);
        return "redirect:/main";
    }

    @PostMapping("/users/tickets/delete")
    public String retrieveTicket(@RequestParam("trainId") Train train,
                                 @AuthenticationPrincipal Client user) {
        Client userFromDatabase = clientService.getClientById(user.getId());
        clientService.deleteClientTicket(train, userFromDatabase);
        return "redirect:/users/tickets";
    }

    @GetMapping("/users/balance")
    public String getUserBalance(Model model,
                                 @AuthenticationPrincipal Client user) {
        Client userFromDatabase = clientService.getClientById(user.getId());
        model.addAttribute("user", userFromDatabase);
        return "balanceReplenishment";
    }

    @PostMapping("/users/balance/add")
    public String updateUserBalance(@RequestParam Integer replenishmentAmount,
                                    @AuthenticationPrincipal Client user) {
        Client userFromDatabase = clientService.getClientById(user.getId());
        clientService.updateClientBalance(replenishmentAmount, userFromDatabase);
        return "redirect:/users/balance";
    }

    @GetMapping("/users/profile")
    public String getUser(@AuthenticationPrincipal User user,
                          Model model) {
        if(user.getClass() == Admin.class) {
            Admin userFromDatabase = adminService.getAdminById(user.getId());
            model.addAttribute("username", userFromDatabase.getUsername());
        } else {
            Client userFromDatabase = clientService.getClientById(user.getId());
            model.addAttribute("username", userFromDatabase.getUsername());
        }
        return "profile";
    }

    @PostMapping("/users/profile")
    public String updateUser(@AuthenticationPrincipal User user,
                             @RequestParam String password
    ) {
        if(user.getClass() == Admin.class) {
            Admin userFromDatabase = adminService.getAdminById(user.getId());
            adminService.updateAdminPassword(userFromDatabase, password);
        } else {
            Client userFromDatabase = clientService.getClientById(user.getId());
            clientService.updateClientPassword(userFromDatabase, password);
        }
        return "redirect:/users/profile";
    }
}