package com.revshop.demo.controller;

import com.revshop.demo.dto.AuthDTO;
import com.revshop.demo.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/buyerRegister")
    private ResponseEntity<String> registerBuyer(@RequestBody Buyer user) {
        String token = userService.registerUser(user);
        return ResponseEntity.status(200).header("Authorization", "Bearer " + token).body("User created");
    }

    @PostMapping("/sellerRegister")
    private ResponseEntity<String> registerSeller(@RequestBody Seller user) {
        String token = userService.registerUser(user);
        return ResponseEntity.status(200).header("Authorization", "Bearer " + token).body("User created");
    }

    @PostMapping("/login")
    private ResponseEntity<UserDTO> loginBuyer(@RequestBody AuthDTO authDTO, HttpServletResponse response) {
        try {
            String token = userService.loginUser(authDTO.getUsername(), authDTO.getPassword());
            User user = userService.getUserByUsername(authDTO.getUsername());
            UserDTO userDTO = userService.makeLoginDTO(user);

            setCookie(response, token);

            return ResponseEntity.ok(userDTO);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

    }

//    @PostMapping("/buyerLogin")
//    private ResponseEntity<UserDTO> loginBuyer(@RequestBody Buyer user, HttpServletResponse response) {
//        String token = userService.loginUser(user);
//        UserDTO userDTO = userService.makeLoginDTO(user);
//
//        setCookie(response, token);
//
//        return ResponseEntity.ok(userDTO);
//    }
//
//    @PostMapping("/sellerLogin")
//    private ResponseEntity<UserDTO> loginSeller(@RequestBody Seller user, HttpServletResponse response) {
//        String token = userService.loginUser(user);
//        UserDTO usersDTO = userService.makeLoginDTO(user);
//
//        setCookie(response, token);
//
//        return ResponseEntity.ok(usersDTO);
//    }

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
