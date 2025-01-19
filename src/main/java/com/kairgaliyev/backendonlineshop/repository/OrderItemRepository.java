package com.kairgaliyev.backendonlineshop.repository;

import com.kairgaliyev.backendonlineshop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
