package com.revshop.demo.controller;

import com.revshop.demo.service.CheckOutService;
import com.revshop.demo.service.UserService;
import com.stripe.service.CheckoutService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckOutController {

    private final UserService userService;
    private final CheckOutService checkOutService;

    public CheckOutController(UserService userService, CheckOutService checkOutService) {
        this.userService = userService;
        this.checkOutService = checkOutService;
    }

    private Long getUserId(Authentication auth) {
        String username = auth.getName();
        return userService.getUserByUsername(username).getId();
    }

    @PostMapping("/checkout")
    ResponseEntity<String> checkOut(Authentication auth, CheckoutRequest checkoutRequest) {
        try {
            checkOutService.checkOut(getUserId(auth), checkoutRequest.getPaymentMethodId());
            return ResponseEntity.ok("Checked out successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Data
    public static class CheckoutRequest {
        private String paymentMethodId;
    }
}
