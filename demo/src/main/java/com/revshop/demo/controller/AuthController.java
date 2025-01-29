package com.revshop.demo.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revshop.demo.dto.UserDTO;
import com.revshop.demo.entity.Buyer;
import com.revshop.demo.entity.Seller;
import com.revshop.demo.entity.User;
import com.revshop.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/buyerRegister")
    private ResponseEntity<String> registerBuyer(@RequestBody Buyer user) {
        String token = userService.registerUser(user);
        return ResponseEntity.status(200).header("Authorization", "Bearer " + token).body("User created");
    }

    @PostMapping("/buyerLogin")
    private ResponseEntity<UserDTO> loginBuyer(@RequestBody Buyer user, HttpServletResponse response) {
        String token = userService.loginUser(user);
        UserDTO usersDTO = userService.makeLoginDTO(user);

        setCookie(response, token);

        return ResponseEntity.ok(usersDTO);
    }

    @PostMapping("/sellerRegister")
    private ResponseEntity<String> registerSeller(@RequestBody Seller user) {
        String token = userService.registerUser(user);
        return ResponseEntity.status(200).header("Authorization", "Bearer " + token).body("User created");
    }

    @PostMapping("/sellerLogin")
    private ResponseEntity<UserDTO> loginSeller(@RequestBody Seller user, HttpServletResponse response) {
        String token = userService.loginUser(user);
        UserDTO usersDTO = userService.makeLoginDTO(user);

        setCookie(response, token);

        return ResponseEntity.ok(usersDTO);
    }

    // testing purposes only
    @GetMapping("/all")
    private ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> dtos = userService.getAllUsers();
        return ResponseEntity.status(200).body(dtos);
    }

    private void setCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // time in seconds
        cookie.setMaxAge(1800);

        response.addCookie(cookie);
    }
}
