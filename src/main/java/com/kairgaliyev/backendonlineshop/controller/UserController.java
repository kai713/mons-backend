package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.UserDto;
import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import com.kairgaliyev.backendonlineshop.entity.mapper.UserMapper;
import com.kairgaliyev.backendonlineshop.resolver.CurrentUser;
import com.kairgaliyev.backendonlineshop.service.intreface.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;
    private final UserMapper userMapper;


    //    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public UserDto getProfile(@CurrentUser UserEntity currentUser, HttpServletRequest request) {
        return userMapper.toUserDto(currentUser);
//        Long userId = (long) request.getAttribute("userId");
//        return userService.getCurrentUser(userId);
    }
}
