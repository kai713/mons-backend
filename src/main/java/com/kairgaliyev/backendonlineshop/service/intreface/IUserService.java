package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.LoginRequest;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.dto.UserDTO;
import com.kairgaliyev.backendonlineshop.enums.UserRole;
import com.kairgaliyev.backendonlineshop.model.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response updateUser(String userId, UserDTO userDTO);

    Response deleteUser(String userId);

    //TODO Response deleteOwnAccount(String userId)  //by current logged;

    Response getUserById(String userId);

    //getCurrentLoggedInUser
    Response getMyInfo(Long id);

    Response changeRoleById(String userId, UserRole role);

    //TODO Response changePasswordById(String userId, String oldPassword, String newPassword);  //by current logged;

    //TODO search user (like in product service interface search)


}
