package com.revshop.demo.service;

import com.revshop.demo.entity.Buyer;
import com.revshop.demo.entity.BuyerOrder;
import com.revshop.demo.entity.CartItem;
import com.revshop.demo.repository.BuyerRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CheckOutService {

    private final CartService cartService;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final BuyerRepository buyerRepository;
    // private final EmailService emailService;
    private final OrderService orderService;

    public CheckOutService(CartService cartService, InventoryService inventoryService, PaymentService paymentService,
            BuyerRepository buyerRepository, OrderService orderService) {
        this.cartService = cartService;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.buyerRepository = buyerRepository;
        this.orderService = orderService;
    }

    public void checkOut(Long buyerId, String paymentMethodId) throws StripeException {
        Buyer buyer = buyerRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("Buyer not found"));
        List<CartItem> cartItems = cartService.getCartItems(buyer.getId());
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("No cart items found for buyer " + buyerId);
        }

        long totalAmount = cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                        .multiply(BigDecimal.valueOf(100))) // Convert to cents
                .mapToLong(BigDecimal::longValue)
                .sum();

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(totalAmount, "usd", paymentMethodId);
        if (!"succeeded".equals(paymentIntent.getStatus())) {
            throw new IllegalStateException("Payment failed!");
        }

        orderService.createOrders(buyer, cartItems);

        cartService.clearCart(buyer);

        /*
         * clear cart, decrement inventory, send emails
         */
    }

}
