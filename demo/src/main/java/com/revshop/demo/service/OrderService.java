package com.revshop.demo.service;

import com.revshop.demo.dto.OrderDTO;
import com.revshop.demo.dto.OrderItemDTO;
import com.revshop.demo.entity.*;
import com.revshop.demo.repository.*;
import jakarta.transaction.Transactional;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final UserRepository userRepository;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    public OrderService(BuyerOrderRepository buyerOrderRepository, SellerOrderRepository sellerOrderRepository, InventoryRepository inventoryRepository, UserRepository userRepository, BuyerRepository buyerRepository, SellerRepository sellerRepository) {
        this.buyerOrderRepository = buyerOrderRepository;
        this.sellerOrderRepository = sellerOrderRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.buyerRepository = buyerRepository;
        this.sellerRepository = sellerRepository;
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
    }

    public List<OrderDTO> getOrdersByUserId(Long userId) {
        User.Role role = userRepository.findRoleById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderDTO> orderDTOs = new ArrayList<>();

        if (role == User.Role.BUYER) {
            List<BuyerOrder> orders = buyerOrderRepository.findAllByBuyerId(userId);
            orderDTOs = orders.stream()
                    .map(order -> {
                        List<OrderItemDTO> items = order.getOrderItems().stream()
                                .map(item -> new OrderItemDTO(
                                        item.getProduct().getId(),
                                        item.getProduct().getName(),
                                        item.getPrice(),
                                        item.getQuantity(),
                                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                                ))
                                .collect(Collectors.toList());

                        BigDecimal total = items.stream()
                                .map(OrderItemDTO::getSubTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        return new OrderDTO(order.getBuyerOrderId(), items, total);
                    })
                    .collect(Collectors.toList());
        } else if (role == User.Role.SELLER) {
            List<SellerOrder> orders = sellerOrderRepository.findAllBySellerId(userId);
            orderDTOs = orders.stream()
                    .map(order -> {
                        List<OrderItemDTO> items = order.getOrderItems().stream()
                                .map(item -> new OrderItemDTO(
                                        item.getProduct().getId(),
                                        item.getProduct().getName(),
                                        item.getPrice(),
                                        item.getQuantity(),
                                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                                ))
                                .collect(Collectors.toList());

                        BigDecimal total = items.stream()
                                .map(OrderItemDTO::getSubTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        return new OrderDTO(order.getSellerOrderId(), items, total);
                    })
                    .collect(Collectors.toList());
        }

        return orderDTOs;
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
