package com.kairgaliyev.backendonlineshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    private String productName;
    private Double productPrice;
    private String imageUrl;

    public CartItem(Cart cart, Product product, int quantity, String productName, Double productPrice, String imageUrl) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
    }
}
