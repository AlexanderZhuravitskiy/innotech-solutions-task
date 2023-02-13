package com.example.innotechsolutionstask.controller;

import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.domain.User;
import com.example.innotechsolutionstask.dto.CaptchaResponseDto;
import com.example.innotechsolutionstask.service.UserService;
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
    private final UserService userService;

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
                          @Valid User user,
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
        if (userService.getUserByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        userService.createUser(user);
        return "login";
    }

    @GetMapping("/users/tickets")
    public String getUserTicketList(@AuthenticationPrincipal User user,
                                    Model model
    ) {
        User userFromDatabase = userService.getUserByUsername(user.getUsername());
        model.addAttribute("trains", userFromDatabase.getTrains());
        return "userTickets";
    }

    @PostMapping("/users/tickets/add")
    public String buyTicket(@RequestParam("trainId") Train train,
                            @AuthenticationPrincipal User user) {
        User userFromDatabase = userService.getUserById(user.getId());
        userService.addUserTickets(train, userFromDatabase);
        return "redirect:/main";
    }

    @PostMapping("/users/tickets/delete")
    public String retrieveTicket(@RequestParam("trainId") Train train,
                                 @AuthenticationPrincipal User user) {
        User userFromDatabase = userService.getUserById(user.getId());
        userService.deleteUserTickets(train, userFromDatabase);
        return "redirect:/users/tickets";
    }

    @GetMapping("/users/balance")
    public String getUserBalance(Model model,
                                 @AuthenticationPrincipal User user) {
        User userFromDatabase = userService.getUserById(user.getId());
        model.addAttribute("user", userFromDatabase);
        return "balanceReplenishment";
    }

    @PostMapping("/users/balance/add")
    public String updateUserBalance(@RequestParam Integer replenishmentAmount,
                                    @AuthenticationPrincipal User user) {
        User userFromDatabase = userService.getUserById(user.getId());
        userService.updateUserBalance(replenishmentAmount, userFromDatabase);
        return "redirect:/users/balance";
    }

    @GetMapping("/users/profile")
    public String getUser(Model model,
                          @AuthenticationPrincipal User user) {
        User userFromDatabase = userService.getUserById(user.getId());
        model.addAttribute("username", userFromDatabase.getUsername());
        return "profile";
    }

    @PostMapping("/users/profile")
    public String updateUser(@AuthenticationPrincipal User user,
                             @RequestParam String password
    ) {
        User userFromDatabase = userService.getUserById(user.getId());
        userService.updatePassword(userFromDatabase, password);
        return "redirect:/users/profile";
    }
}