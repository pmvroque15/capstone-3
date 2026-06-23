package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;


//TODO clean the code for accessing principal's userId -> add to service class
//TODO add comments on each endpoint
@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class ShoppingCartController
{

    private ShoppingCartService shoppingCartService;
    private UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ShoppingCart> getCart(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ShoppingCart cart = shoppingCartService.getCart(principal);

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/products/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ShoppingCart> addProductToCart(@PathVariable int productId, Principal principal) {



        ShoppingCart updatedCart =  shoppingCartService.addProduct(principal, productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ShoppingCart> updateItem(Principal principal, @PathVariable int productId, @RequestBody ShoppingCartItem item) {
        
        ShoppingCart cart = shoppingCartService.updateItem(principal, productId, item);

        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ShoppingCart> deleteProductFromCart(Principal principal) {
        int userId = shoppingCartService.getUserId(principal);
        shoppingCartService.clearItems(userId);
        ShoppingCart cart = shoppingCartService.getCart(principal);

        return  ResponseEntity.status(HttpStatus.OK).body(cart);
    }

}
