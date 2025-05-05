package com.kairgaliyev.backendonlineshop.repository;

import com.kairgaliyev.backendonlineshop.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByNameContainingOrDescriptionContaining(String name, String description);

    Optional<ProductEntity> findByName(String name);
}
