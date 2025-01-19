package com.kairgaliyev.backendonlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.kairgaliyev.backendonlineshop.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}