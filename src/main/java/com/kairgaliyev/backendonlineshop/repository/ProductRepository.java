package com.kairgaliyev.backendonlineshop.repository;

import com.kairgaliyev.backendonlineshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);

    Optional<Product> findByName(String name);
}
