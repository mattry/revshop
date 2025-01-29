package com.revshop.demo.dto;

import com.revshop.demo.entity.User.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String firstName;

    private String lastName;

    private String username;

    private Long userId;

    private Role role;
}
