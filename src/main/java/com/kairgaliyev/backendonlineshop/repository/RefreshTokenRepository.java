package com.kairgaliyev.backendonlineshop.repository;

import com.kairgaliyev.backendonlineshop.model.RefreshToken;
import com.kairgaliyev.backendonlineshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByToken(String refreshToken);
}
