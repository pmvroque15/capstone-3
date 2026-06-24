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


//TODO add comments on each endpoint
@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ShoppingCartController
{

    private ShoppingCartService shoppingCartService;


    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping

    public ResponseEntity<ShoppingCart> getCart(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ShoppingCart cart = shoppingCartService.getCart(principal);

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<ShoppingCart> addProductToCart(@PathVariable int productId, Principal principal) {

        ShoppingCart updatedCart =  shoppingCartService.addProduct(principal, productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ShoppingCart> updateItem(Principal principal, @PathVariable int productId, @RequestBody ShoppingCartItem item) {

        ShoppingCart cart = shoppingCartService.updateItem(principal, productId, item);

        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    public ResponseEntity<ShoppingCart> deleteProductFromCart(Principal principal) {
        int userId = shoppingCartService.getUserId(principal);
        shoppingCartService.clearItems(userId);
        ShoppingCart cart = shoppingCartService.getCart(principal);

        return  ResponseEntity.status(HttpStatus.OK).body(cart);
    }

}
