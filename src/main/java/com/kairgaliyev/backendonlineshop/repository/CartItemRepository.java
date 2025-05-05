package com.kairgaliyev.backendonlineshop.repository;

import com.kairgaliyev.backendonlineshop.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
}
