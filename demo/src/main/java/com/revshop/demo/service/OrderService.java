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

    public OrderService(BuyerOrderRepository buyerOrderRepository, SellerOrderRepository sellerOrderRepository, InventoryRepository inventoryRepository) {
        this.buyerOrderRepository = buyerOrderRepository;
        this.sellerOrderRepository = sellerOrderRepository;
        this.inventoryRepository = inventoryRepository;
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
