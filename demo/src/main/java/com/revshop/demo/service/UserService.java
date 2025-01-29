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

    @Autowired
    public UserService(JwtUtil jwtUtil, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public String registerUser(User user) {
        if (user.getRole() == null) {
            user.setRole(User.Role.BUYER); // Default to BUYER
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return jwtUtil.generateToken(user.getUsername());
    }

    public String loginUser(String username, String password) throws AuthenticationException {
        if (username == null || password == null ||
            username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        if (user.getRole() == null) {
            user.setRole(User.Role.BUYER);
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
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username does not exist"));
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return mapToDTO(user);
        } else {
            throw new RuntimeException("Invalid user id");
        }
    }
}
