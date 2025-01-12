package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.LoginRequest;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.enums.UserRole;
import com.kairgaliyev.backendonlineshop.model.User;

public interface MyUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

    Response changeRoleById(String userId, UserRole role);
}
