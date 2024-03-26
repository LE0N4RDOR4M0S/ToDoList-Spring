package com.example.demo.Controller;

import java.util.Date;
import java.security.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Configurer.TokenService;
import com.example.demo.Configurer.UserLoginDto;
import com.example.demo.Domain.User;
import com.example.demo.Service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@SuppressWarnings("deprecation")
@Controller
@RequestMapping
public class UserController {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    private UserService userService;

    @Autowired
    TokenService tokenService;

    @GetMapping("/login")
    public String getLogin() {
        return "login.html";
    }

    @GetMapping("/register")
    public String getRegister(){
        return "register.html";
    }

    /**
     * Endpoint para criar um novo usuário
     *
     * @param userDetails Detalhes do usuário a serem registrados
     * @return O usuário criado
     */
    @PostMapping("/register")
    public String registerUser(UserLoginDto user) {
        if (userService.getUserByUsername(user.getUsername()) == null) {
            User userDetails = new User(null, user.getUsername(), user.getPassword());
            userService.createUser(userDetails);
            return "redirect:/login";
        }else{
            return "errorname.html";
        }
    }

    /**
     * Endpoint para fazer login
     *
     * @param userLoginDto Detalhes do usuário para fazer login
     */
    @PostMapping("/login")
    public String loginUser(@ModelAttribute("userLoginDto") UserLoginDto userLoginDto, HttpServletResponse response) {
        User user = userService.getUserByUsername(userLoginDto.getUsername());
        if (user != null && userService.checkPassword(user, userLoginDto.getPassword())) {
            var token = tokenService.generateToken(user);

            Cookie cookie = new Cookie("jwt-token", token);
            cookie.setSecure(true); // Set this to true if you're using HTTPS
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(600);

            response.addCookie(cookie);

            return "redirect:/task/tasks/"+user.getId();
        } else {
            return "redirect:/error";
        }
    }

}
