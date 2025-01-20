package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.model.Cart;

import java.util.List;
import java.util.stream.Collectors;

public class CartDTO {
    private Long id;
    private List<CartItemDTO> cartItems;

    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList());
    }

    public CartDTO() {
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
