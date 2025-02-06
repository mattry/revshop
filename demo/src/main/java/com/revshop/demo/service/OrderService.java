package com.revshop.demo.service;

import com.revshop.demo.entity.*;
import com.revshop.demo.repository.BuyerOrderRepository;
import com.revshop.demo.repository.InventoryRepository;
import com.revshop.demo.repository.SellerOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final BuyerOrderRepository buyerOrderRepository;
    private final SellerOrderRepository sellerOrderRepository;
    private final InventoryRepository inventoryRepository;
    private final EmailService emailService;

    public OrderService(BuyerOrderRepository buyerOrderRepository, SellerOrderRepository sellerOrderRepository, InventoryRepository inventoryRepository, EmailService emailService) {
        this.buyerOrderRepository = buyerOrderRepository;
        this.sellerOrderRepository = sellerOrderRepository;
        this.inventoryRepository = inventoryRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void createOrders(Buyer buyer, List<CartItem> items) {
        BuyerOrder buyerOrder = new BuyerOrder();
        buyerOrder.setBuyer(buyer);
        buyerOrder = buyerOrderRepository.save(buyerOrder);

        List<OrderItem> orderItems = new ArrayList<>();
        Map<Seller, SellerOrder> sellerOrders = new HashMap<>();

        for (CartItem item : items) {
            Seller seller = item.getProduct().getSeller();
            SellerOrder sellerOrder = sellerOrders.computeIfAbsent(seller, s -> {
                SellerOrder newOrder = new SellerOrder();
                newOrder.setSeller(seller);
                return sellerOrderRepository.save(newOrder);
            });

            OrderItem orderItem = new OrderItem();
            orderItem.setBuyerOrder(buyerOrder);
            orderItem.setSellerOrder(sellerOrder);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProduct(item.getProduct());
            orderItem.setPrice(item.getProduct().getPrice());

            orderItems.add(orderItem);
        }

        buyerOrder.setOrderItems(orderItems);
        buyerOrderRepository.save(buyerOrder);

        sendOrderEmails(buyer, buyerOrder, sellerOrders);

    }

    private void sendOrderEmails(Buyer buyer, BuyerOrder order, Map<Seller, SellerOrder> sellerOrders) {
        // Email to Buyer
        String buyerEmail = buyer.getEmail();
        String buyerSubject = "Order Confirmation - RevShop";
        String buyerMessage = "Hello " + buyer.getUsername() + ",\n\n"
                + "Your order #" + order.getBuyerOrderId() + " has been successfully placed.\n"
                + "We will notify you once it has been shipped.\n\nThank you for shopping with RevShop!";
        emailService.sendEmail(buyerEmail, buyerSubject, buyerMessage);

        // Email to Sellers
        for (Seller seller : sellerOrders.keySet()) {
            String sellerEmail = seller.getEmail();
            String sellerSubject = "New Order Received - RevShop";
            String sellerMessage = "Hello " + seller.getUsername() + ",\n\n"
                    + "You have received a new order (#" + order.getBuyerOrderId() + ").\n"
                    + "Please prepare the items for shipping.\n\nThank you for selling on RevShop!";
            emailService.sendEmail(sellerEmail, sellerSubject, sellerMessage);
        }
    }

//    public void createSellerOrders(BuyerOrder buyerOrder) {
//        Map<Seller, List<OrderItem>> groupedBySeller = buyerOrder.getOrderItems().stream()
//                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller()));
//
//        groupedBySeller.forEach((seller, orderItems) -> {
//            SellerOrder sellerOrder = new SellerOrder();
//            sellerOrder.setSeller(seller);
//            sellerOrder.setOrderItems(orderItems);
//            sellerOrderRepository.save(sellerOrder);
//
//        });
//    }
}
