package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.AuthResponse;
import com.kairgaliyev.backendonlineshop.dto.LoginRequest;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import com.kairgaliyev.backendonlineshop.service.intreface.IRefreshTokenService;
import com.kairgaliyev.backendonlineshop.service.intreface.IUserService;
import com.kairgaliyev.backendonlineshop.utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//TODO: test due to changed jwt logic
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final IUserService userService;
    private final IRefreshTokenService refreshTokenService;
    private final JWTUtils jwtUtils;

    //TODO UserRequest
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody UserEntity user) {
        log.info("Registering user: {}", user);
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = userService.login(request);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/v1/auth")
                .maxAge(5 * 60)
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(authResponse);
    }

    //TODO: с new BadRequestException передавать, не обматывать таким образом ответ: "ResponseEntity<?>"
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token is missing"));
        }
        //TODO: рефактор, вынести в service
        return refreshTokenService.findByToken(refreshToken)
                .filter(refreshTokenService::validateRefreshToken)
                .map(token -> {
//                    UserEntity user = token.getUser();
//                    String newAccessToken = jwtUtils.generateToken(user);

                    return ResponseEntity.ok()
                            .body(Map.of("accessToken", null));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid refresh token")));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getTokenFromCookie(request, "refreshToken");

        if (refreshToken != null) {
            refreshTokenService.invalidateRefreshToken(refreshToken);
        }

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok().build();
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
