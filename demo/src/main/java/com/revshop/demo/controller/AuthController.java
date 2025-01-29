package com.revshop.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revshop.demo.dto.UserDTO;
import com.revshop.demo.entity.Buyer;
import com.revshop.demo.entity.Seller;
import com.revshop.demo.entity.User;
import com.revshop.demo.service.UserService;

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
    private ResponseEntity<UserDTO> loginBuyer(@RequestBody Buyer user) {
        String token = userService.loginUser(user);
        UserDTO usersDTO = userService.makeLoginDTO(user);
        return ResponseEntity.status(200).header("Authorization", "Bearer "+token).body(usersDTO);
    }

    @PostMapping("/sellerRegister")
    private ResponseEntity<String> registerSeller(@RequestBody Seller user) {
        String token = userService.registerUser(user);
        return ResponseEntity.status(200).header("Authorization", "Bearer " + token).body("User created");
    }

    @PostMapping("/sellerLogin")
    private ResponseEntity<UserDTO> loginSeller(@RequestBody Seller user) {
        String token = userService.loginUser(user);
        UserDTO usersDTO = userService.makeLoginDTO(user);
        return ResponseEntity.status(200).header("Authorization", "Bearer "+token).body(usersDTO);
    }
}