package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Domain.User;
import com.example.demo.Service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint para criar um novo usuário
     *
     * @param userDetails Detalhes do usuário a serem registrados
     * @return O usuário criado
     */
    @PostMapping("/register")
    public User registerUser(@RequestBody User userDetails) {
        String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(encodedPassword);
        return userService.createUser(userDetails);
    }

    /**
     * Endpoint para fazer login
     *
     * @param userLogin Detalhes do usuário para fazer login
     * @return O usuário autenticado
     */
    @PostMapping("/login")
    public User loginUser(@RequestBody User userLogin) {
        User user = userService.getUserByUsername(userLogin.getName());
        if (user != null && userService.checkPassword(user, userLogin.getPassword())) {
            return user;
        } else {
            return null;
        }
    }
}
