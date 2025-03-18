package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.AuthResponse;
import com.kairgaliyev.backendonlineshop.dto.LoginRequest;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.model.RefreshToken;
import com.kairgaliyev.backendonlineshop.model.User;
import com.kairgaliyev.backendonlineshop.service.intreface.IRefreshTokenService;
import com.kairgaliyev.backendonlineshop.service.intreface.IUserService;
import com.kairgaliyev.backendonlineshop.utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//TODO: test due to changed jwt logic
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final IRefreshTokenService refreshTokenService;
    private final JWTUtils jwtUtils;

    //TODO UserRequest
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = userService.login(request);

        // Устанавливаем refreshToken в HttpOnly куку
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 дней
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token is missing"));
        }

        return refreshTokenService.findByToken(refreshToken)
                .filter(refreshTokenService::validateRefreshToken)
                .map(token -> {
                    User user = token.getUser();
                    String newAccessToken = jwtUtils.generateToken(user);
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getEmail());

                    ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken.getToken())
                            .httpOnly(true)
                            .secure(true) // В продакшен
                            .path("/")
                            .maxAge(7 * 24 * 60 * 60)
                            .sameSite("Strict")
                            .build();

                    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                    return ResponseEntity.ok()
                            .body(Map.of("accessToken", newAccessToken));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid refresh token")));
    }
}
