package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.AuthResponse;
import com.kairgaliyev.backendonlineshop.entity.RefreshTokenEntity;
import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import com.kairgaliyev.backendonlineshop.exception.BadRequestException;
import com.kairgaliyev.backendonlineshop.repository.RefreshTokenRepository;
import com.kairgaliyev.backendonlineshop.repository.UserRepository;
import com.kairgaliyev.backendonlineshop.service.intreface.IRefreshTokenService;
import com.kairgaliyev.backendonlineshop.utils.JWTService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public RefreshTokenEntity createRefreshToken(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow();

        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        //TODO: use Mapstruct
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setExpiryDate(Instant.now().plus(5, ChronoUnit.MINUTES));

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .filter(token -> token.getExpiryDate().isAfter(Instant.now()))
                .map(token -> {
                    UserEntity user = token.getUser();
                    String newAccessToken = jwtService.generateToken(user);
                    return AuthResponse.builder()
                            .accessToken(newAccessToken)
                            .build();
                })
                .orElseThrow(() -> new BadRequestException("invalid or expired refresh token", "error.bad_request"));
    }

    @Override
    public void invalidateRefreshToken(HttpServletRequest request) {

        String refreshToken = getTokenFromCookie(request, "refreshToken");
        refreshTokenRepository.deleteByToken(refreshToken);

    }

    private String getTokenFromCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

