package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;
import org.yearup.repository.ShoppingCartRepository;

import java.security.Principal;
import java.util.List;

@Service
public class ShoppingCartService
{
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

    public ShoppingCart addProduct(int userId, int id) {
       CartItem product = new CartItem();

       product.setUserId(userId);
       product.setProductId(id);

       CartItem userAndProduct = shoppingCartRepository.findByUserIdAndProductId(userId, id);

       if (userAndProduct == null) {
           shoppingCartRepository.save(product);
       } else {
           int quantity = userAndProduct.getQuantity();
           userAndProduct.setQuantity(quantity + 1);
           shoppingCartRepository.save(userAndProduct);
       }

       return getByUserId(userId);
    }

    @Transactional
    public void clearItems(int userId) {
        shoppingCartRepository.deleteByUserId(userId);
    }

    public ShoppingCart updateItem(int userId, int productId, ShoppingCartItem item) {
        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);
        cartItem.setQuantity(item.getQuantity());
        shoppingCartRepository.save(cartItem);

        return getByUserId(userId);
    }
}
