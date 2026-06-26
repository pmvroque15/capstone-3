package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;
import org.yearup.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderLineItemsRepository orderLineItemsRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(ShoppingCartRepository shoppingCartRepository, OrderRepository orderRepository, UserRepository userRepository, ProfileRepository profileRepository, ProductRepository productRepository, OrderLineItemsRepository orderLineItemsRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.productRepository = productRepository;
        this.orderLineItemsRepository = orderLineItemsRepository;
    }

    /**
     * Creates a new order for the specified user by converting all items
     * currently in their shopping cart into order line items.
     * After order is successfully created, the shopping cart is cleared.
     *
     * @param name the username of the authenticated user
     * @return the newly created order
     *
     */
    @Transactional
    public Order checkout(String name) {

        User user = userRepository.findByUsername(name);
        int userId = user.getId();

        List<CartItem> items = shoppingCartRepository.findByUserId(userId);
        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot checkout with an empty cart");
        }

        Profile userProfile = profileRepository.findByUserId(userId);

        Order order = new Order();
        order.setUserId(userProfile.getUserId());
        order.setDate(LocalDateTime.now());
        order.setAddress(userProfile.getAddress());
        order.setCity(userProfile.getCity());
        order.setState(userProfile.getState());
        order.setZip(userProfile.getZip());
        order.setShippingAmount(0);
        Order savedOrder = orderRepository.save(order);

        for (CartItem item : items) {
            OrderLineItems orderLineItems = new OrderLineItems();
            orderLineItems.setOrderId(order.getOrderId());
            orderLineItems.setProductId(item.getProductId());
            orderLineItems.setQuantity(item.getQuantity());
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            orderLineItems.setSalesPrice(product.getPrice());
            orderLineItems.setDiscount(0);
            orderLineItemsRepository.save(orderLineItems);

        }

        shoppingCartRepository.deleteByUserId(userId);

        return savedOrder;
    }
}

