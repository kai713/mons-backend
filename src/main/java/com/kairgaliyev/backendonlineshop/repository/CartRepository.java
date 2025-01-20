package com.kairgaliyev.backendonlineshop.repository;

import com.kairgaliyev.backendonlineshop.model.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = {"cartItems.product", "user"})

//    SELECT c.*, ci.*, p.*, u.*
//FROM cart c
//JOIN cart_items ci ON c.id = ci.cart_id
//JOIN product p ON ci.product_id = p.id
//JOIN users u ON c.user_id = u.id
//WHERE c.user_id = ?

    Optional<Cart> findByUserId(Long userId);
}
