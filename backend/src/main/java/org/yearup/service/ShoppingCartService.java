package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yearup.models.*;
import org.yearup.repository.ShoppingCartRepository;

import java.security.Principal;
import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService, UserService userService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        ShoppingCart shoppingCart = new ShoppingCart();

        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);

        for (CartItem cartItem: cartItems) {
            Product product = productService.getById(cartItem.getProductId());

            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(cartItem.getQuantity());

            shoppingCart.add(shoppingCartItem);
        }
        return shoppingCart;
    }

    public void addProduct(int userId, int id) {
        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, id);

        //if product is already in cart
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(id);
            cartItem.setQuantity(1);
        }

        shoppingCartRepository.save(cartItem);
    }

    @Transactional
    public ShoppingCart clearItems(Principal principal) {
        User user = userService.getByUserName(principal.getName());

        shoppingCartRepository.deleteByUserId(user.getId());

        return new ShoppingCart();
    }

    // add additional methods here

}
