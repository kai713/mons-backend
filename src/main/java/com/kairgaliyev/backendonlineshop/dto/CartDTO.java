package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO implements Serializable {
    private Long id;
    private List<CartItemDTO> cartItems;

    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList());
    }
}