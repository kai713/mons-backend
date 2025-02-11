package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItemDTO implements Serializable {
    private Long id;
    private Long productId;
    private int quantity;

    //TODO: refactor neednt attributes
    private String productName;
    private Double productPrice;
    private String imageUrl;

    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.productPrice = cartItem.getProduct().getPrice();
        this.quantity = cartItem.getQuantity();
    }
}