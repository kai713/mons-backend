package com.kairgaliyev.backendonlineshop.repository;

import com.kairgaliyev.backendonlineshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
