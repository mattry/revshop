package com.revshop.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.revshop.demo.dto.UserDTO;
import com.revshop.demo.entity.User;
import com.revshop.demo.repository.UserRepository;
import com.revshop.demo.security.JwtUtil;

@Service
public class UserService {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    @Autowired
    public UserService(JwtUtil jwtUtil, UserRepository userRepository, AuthenticationManager authenticationManager, EmailService emailService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;  // ✅ Inject EmailService
    }

    public String registerUser(User user) {
        if (user.getRole() == null) {
            user.setRole(User.Role.BUYER); // Default to BUYER
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        sendWelcomeEmail(user);
        return jwtUtil.generateToken(user.getUsername());
    }

    public String loginUser(String username, String password) throws AuthenticationException {
        if (username == null || password == null ||
                username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtUtil.generateToken(username);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setUserId(user.getId());
        dto.setRole(user.getRole().name());
        return dto;
    }

    public UserDTO makeLoginDTO(User user) {
        User loggedIn = getUserByUsername(user.getUsername());
        return mapToDTO(loggedIn);
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> dtos = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            dtos.add(mapToDTO(user));
        }

        return dtos;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username does not exist"));
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return mapToDTO(user);
        } else {
            throw new RuntimeException("Invalid user id");
        }
    }

    private void sendWelcomeEmail(User user) {
        String subject = "Welcome to RevShop!";
        String message = "Hello " + user.getFirstName() + ",\n\n"
                + "Thank you for registering at RevShop. We're excited to have you on board!\n\n"
                + "You can now log in and start shopping.\n\n"
                + "Best regards,\nRevShop Team";

        emailService.sendEmail(user.getEmail(), subject, message);
    }
}
