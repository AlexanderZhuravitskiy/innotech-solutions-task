package com.example.innotechsolutionstask.controller;

import com.example.innotechsolutionstask.domain.Role;
import com.example.innotechsolutionstask.domain.User;
import com.example.innotechsolutionstask.service.UserService;
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
    private final UserService userService;

    @GetMapping("/users")
    public String getUserList(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("users", userList);
        return "userList";
    }

    @GetMapping("/users/{user}")
    public String editUser(@PathVariable User user,
                           Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("/users/update")
    public String saveUser(@RequestParam String username,
                           @RequestParam Map<String, String> form,
                           @RequestParam("userId") User user
    ) {
        userService.updateUserPasswordAndRoles(username, form, user);
        return "redirect:/users";
    }
}