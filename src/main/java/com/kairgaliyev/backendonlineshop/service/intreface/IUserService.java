package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.AuthResponse;
import com.kairgaliyev.backendonlineshop.dto.LoginRequest;
import com.kairgaliyev.backendonlineshop.dto.UserDto;
import com.kairgaliyev.backendonlineshop.dto.UserRequest;
import jakarta.servlet.http.Cookie;

import java.util.UUID;

public interface IUserService {
    UserRequest register(UserRequest user, Cookie cooke);

    AuthResponse login(LoginRequest loginRequest);

    UserDto getCurrentUser(Long userId);

    UUID unauthorizedUserUUID();
}
