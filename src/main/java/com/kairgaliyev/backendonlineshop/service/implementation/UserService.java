package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.AuthResponse;
import com.kairgaliyev.backendonlineshop.dto.LoginRequest;
import com.kairgaliyev.backendonlineshop.dto.UserDto;
import com.kairgaliyev.backendonlineshop.dto.UserRequest;
import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import com.kairgaliyev.backendonlineshop.entity.mapper.UserMapper;
import com.kairgaliyev.backendonlineshop.exception.BadRequestException;
import com.kairgaliyev.backendonlineshop.repository.UserRepository;
import com.kairgaliyev.backendonlineshop.service.intreface.IUserService;
import com.kairgaliyev.backendonlineshop.utils.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JWTService jwtService;
    private final UserMapper userMapper;

    @Override
    public UserRequest register(UserRequest user) {
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new BadRequestException("User already exists in system", "error.bad_request");
        }

        UserEntity userEntity = userMapper.toNewUserEntity(user);
        userEntity = userRepository.save(userEntity);
        UserRequest userRequest = userMapper.toUserRequest(userEntity);
        return userRequest;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.email(), request.password());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Override
    public UserDto getCurrentUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("not found", "error.not_found", "user"));

        UserDto userDto = userMapper.toUserDto(user);

        return userDto;
    }
}
