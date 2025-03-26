package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.CartDTO;
import com.kairgaliyev.backendonlineshop.dto.CartItemDTO;
import com.kairgaliyev.backendonlineshop.dto.IResponse;
import com.kairgaliyev.backendonlineshop.service.intreface.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestAttribute("userId") Long userId) {
        CartDTO cartDTO = cartService.getCartById(userId);
        return ResponseEntity.status(HttpStatus.FOUND).body(cartDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemDTO> addProduct(
            @RequestAttribute("userId") Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        CartItemDTO cartItemDTO = cartService.addProduct(userId, productId, quantity);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(cartItemDTO);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<IResponse> removeProduct(
            @RequestAttribute("userId") Long userId,
            @RequestParam Long productId) {
        IResponse response = cartService.removeProduct(userId, productId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<IResponse> clearCart(@RequestAttribute("userId") Long userId) {
        IResponse response = cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<CartItemDTO> updateQuantity(
            @RequestAttribute("userId") Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        CartItemDTO cartItemDTO = cartService.updateProductQuantity(userId, productId, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(cartItemDTO);
    }
}
