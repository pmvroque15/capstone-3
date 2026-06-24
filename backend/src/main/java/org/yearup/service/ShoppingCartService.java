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
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;
    private UserService userService;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService, UserService userService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
        this.userService = userService;
    }

    // helper method
    public int getUserId(Principal principal) {

        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        return user.getId();
    }

    public ShoppingCart getCart(Principal principal)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        ShoppingCart shoppingCart = new ShoppingCart();

        List<CartItem> cartItems = shoppingCartRepository.findByUserId(getUserId(principal));

        for (CartItem cartItem: cartItems) {
            Product product = productService.getById(cartItem.getProductId());

            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(cartItem.getQuantity());

            shoppingCart.add(shoppingCartItem);
        }
        return shoppingCart;
    }

    public ShoppingCart addProduct(Principal principal, int productId) {
        CartItem product = new CartItem();

       product.setUserId(getUserId(principal));
       product.setProductId(productId);

       CartItem userAndProduct = shoppingCartRepository.findByUserIdAndProductId(getUserId(principal), productId);

       if (userAndProduct == null) {
           shoppingCartRepository.save(product);
       } else {
           int quantity = userAndProduct.getQuantity();
           userAndProduct.setQuantity(quantity + 1);
           shoppingCartRepository.save(userAndProduct);
       }

       return getCart(principal);
    }

    @Transactional
    public void clearItems(int userId) {
        shoppingCartRepository.deleteByUserId(userId);
    }

    public ShoppingCart updateItem(Principal principal, int productId, ShoppingCartItem item) {
        int userId = getUserId(principal);
        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);
        cartItem.setQuantity(item.getQuantity());
        shoppingCartRepository.save(cartItem);

        return getCart(principal);
    }


}
