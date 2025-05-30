package org.api.pjaidapp.controller;

import org.api.pjaidapp.repository.UserRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login/{id}")
    public String login(@PathVariable Long id) {
        return setLoginStatus(id, true);
    }

    @PostMapping("/logout/{id}")
    public String logout(@PathVariable Long id) {
        return setLoginStatus(id, false);
    }

    private String setLoginStatus(Long id, boolean status) {
        return userRepository.findById(id).map(user -> {
            user.setLoggedIn(status);
            userRepository.save(user);
            return "User " + user.getUserName() + " is now " + (status ? "logged in" : "logged out");
        }).orElse("User not found.");
    }
}
