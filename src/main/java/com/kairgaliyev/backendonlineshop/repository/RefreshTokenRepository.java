package com.kairgaliyev.backendonlineshop.repository;

import com.kairgaliyev.backendonlineshop.entity.RefreshTokenEntity;
import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByUser(UserEntity user);

    void deleteByToken(String refreshToken);
}
