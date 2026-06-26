package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yearup.models.*;
import org.yearup.repository.ShoppingCartRepository;

import java.security.Principal;
import java.util.List;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService, UserService userService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
        this.userService = userService;
    }

    /**
     * Retrieves the ID of the currently authenticated user
     *
     * @param principal the authenticated user
     * @return the iD of the authenticated user
     */
    public int getUserId(Principal principal) {

        String userName = principal.getName();
        User user = userService.getByUserName(userName);

        return user.getId();
    }

    /**
     * Retrieve's the shopping cart for the currently authenticated user.
     * The cart is populated with the user's cart item and their
     * corresponding product information.
     *
     * @param principal the authenticated user
     * @return the shopping cart for the authenticated user
     */
    public ShoppingCart getCart(Principal principal) {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        ShoppingCart shoppingCart = new ShoppingCart();

        List<CartItem> cartItems = shoppingCartRepository.findByUserId(getUserId(principal));

        for (CartItem cartItem : cartItems) {
            Product product = productService.getById(cartItem.getProductId());

            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(cartItem.getQuantity());

            shoppingCart.add(shoppingCartItem);
        }
        return shoppingCart;
    }

    /**
     * Adds a product to the authenticated user's shopping cart.
     * If the product is already exists in the cart, its quantity
     * increased by 1. Otherwise, a new cart item is created.
     *
     * @param principal the authenticated user
     * @param productId of the product that user wants to add in their current shopping cart
     * @return the updated cart with its contents
     *
     */
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

    /**
     * Updates the quantity of the specified product in the authenticated user's shopping cart.
     *
     * @param principal the authenticated user
     * @param productId the ID of the product to update
     * @param item that shopping cart item containing the updated quantity
     * @return the updated cart.
     */
    public ShoppingCart updateItem(Principal principal, int productId, ShoppingCartItem item) {
        int userId = getUserId(principal);
        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);
        cartItem.setQuantity(item.getQuantity());
        shoppingCartRepository.save(cartItem);

        return getCart(principal);
    }

    /**
     * Removes all items from the specified user's shopping cart.
     *
     * @param userId the ID of teh user whose shopping cart will be cleared
     */
    @Transactional
    public void clearItems(int userId) {
        shoppingCartRepository.deleteByUserId(userId);
    }

}
