package com.revshop.demo.controller;

import com.revshop.demo.dto.OrderDTO;
import com.revshop.demo.service.OrderService;
import com.revshop.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    private Long getUserId(Authentication auth) {
        String username = auth.getName();
        return userService.getUserByUsername(username).getId();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getOrders(Authentication authentication) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(getUserId(authentication));
        return ResponseEntity.ok(orders);
    }

}
