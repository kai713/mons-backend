package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.AuthResponse;
import com.kairgaliyev.backendonlineshop.dto.LoginRequest;
import com.kairgaliyev.backendonlineshop.dto.UserRequest;
import com.kairgaliyev.backendonlineshop.service.intreface.IRefreshTokenService;
import com.kairgaliyev.backendonlineshop.service.intreface.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final IUserService userService;
    private final IRefreshTokenService refreshTokenService;

    //TODO UserRequest
    @PostMapping("/register")
    public ResponseEntity<UserRequest> register(@RequestBody UserRequest user, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = null;
        try {
            cookie = new Cookie(request.getCookies()[0].getName(), request.getCookies()[0].getValue());
        } catch (Exception e) {
            log.info("Error while registering user: {}", e.getMessage());
        }
        UserRequest userRequest = userService.register(user, cookie);
        clearCookieValue("UUID", response);
        return ResponseEntity.ok().body(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        log.info("Login request: {}", request);
        AuthResponse authResponse = userService.login(request);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/v1/auth")
                .maxAge(5 * 60)
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        log.info("Refreshing token: {}", refreshToken);
        AuthResponse authResponse = refreshTokenService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Logging out user: {}", request.getAttribute("userId"));

        refreshTokenService.invalidateRefreshToken(request);
        clearCookieValue("refreshToken", response);

        return ResponseEntity.ok().build();
    }

    private void clearCookieValue(String cookieName, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
