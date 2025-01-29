package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.service.intreface.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Response> getCart(@PathVariable("userId") Long userId) {
        Response response = cartService.getCartById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addProduct(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        Response response = cartService.addProduct(userId, productId, quantity);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Response> removeProduct(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        Response response = cartService.removeProduct(userId, productId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable("userId") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateQuantity(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateProductQuantity(userId, productId, quantity));
    }
}
