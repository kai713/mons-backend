package com.kairgaliyev.backendonlineshop.security;

import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import com.kairgaliyev.backendonlineshop.enums.UserRole;
import com.kairgaliyev.backendonlineshop.repository.UserRepository;
import com.kairgaliyev.backendonlineshop.service.CustomUserDetailsService;
import com.kairgaliyev.backendonlineshop.utils.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwtToken = authHeader.substring(7);
                String userEmail = jwtService.extractUsername(jwtToken);
                Long userId = jwtService.extractUserId(jwtToken);
                String role = jwtService.extractRole(jwtToken);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                    if (jwtService.isValidToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken token =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(token);
                        SecurityContextHolder.setContext(context);

                        request.setAttribute("userId", userId);
                        request.setAttribute("role", role);
                        filterChain.doFilter(request, response);
                        return; // Авторизован — больше ничего не нужно
                    }
                }
            }

            // Если не авторизован или токен невалиден — fallback на anonymous guest
            String uuid = getUuidFromCookies(request);

            if (uuid != null) {
                userRepository.findByName(uuid).ifPresentOrElse(
                        user -> setSpringSecurityContext(new CustomUserDetails(user), request),
                        () -> createGuest(uuid, response, request)
                );
            } else {
                String newUuid = UUID.randomUUID().toString();
                createGuest(newUuid, response, request);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("""
                    {
                      "status": 401,
                      "message": "Expired JWT token",
                      "timestamp": "%s"
                    }
                    """.formatted(LocalDateTime.now()));
        }
    }

    private String getUuidFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("UUID"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private void createGuest(String uuid, HttpServletResponse response, HttpServletRequest request) {
        UserEntity guest = new UserEntity();
        guest.setName(uuid);
        guest.setRole(UserRole.UNAUTHORIZED);
        guest.setCreatedAt(LocalDateTime.now());
        userRepository.save(guest);

        setSpringSecurityContext(new CustomUserDetails(guest), request);

        Cookie cookie = new Cookie("UUID", uuid);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 дней
        response.addCookie(cookie);
    }

    private void setSpringSecurityContext(UserDetails user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
    }
}