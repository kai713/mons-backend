package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.LoginRequest;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.dto.UserDTO;
import com.kairgaliyev.backendonlineshop.enums.UserRole;
import com.kairgaliyev.backendonlineshop.exception.MyException;
import com.kairgaliyev.backendonlineshop.model.Cart;
import com.kairgaliyev.backendonlineshop.model.User;
import com.kairgaliyev.backendonlineshop.repository.CartRepository;
import com.kairgaliyev.backendonlineshop.repository.UserRepository;
import com.kairgaliyev.backendonlineshop.service.intreface.IUserService;
import com.kairgaliyev.backendonlineshop.utils.JWTUtils;
import com.kairgaliyev.backendonlineshop.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Response register(User user) {
        Response response = new Response();
        try {
            //check if user already co not have role
            if (user.getRole() == null || user.getRole().toString().isBlank()) {
                user.setRole(UserRole.USER);
            }
            //check if user already have in db
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new MyException(user.getEmail() + "Already Exists");
            }
            //build response and userDTO
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);

            //Cart part
            Cart cart = new Cart();
            cart.setUser(user);

            cartRepository.save(cart);
        } catch (MyException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During USer Registration " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try {
            //check user from db and generate token
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new MyException("user Not found"));

            var token = jwtUtils.generateToken(user);
            var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

            //build response
            response.setStatusCode(200);
            response.setToken(token);
            response.setRefreshToken(refreshToken.getToken()); // Добавляем refresh token
            response.setRole(user.getRole().toString());
            response.setExpirationTime("1 hour");
            response.setMessage("successful");

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error Occurred During USer Login " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {

        Response response = new Response();
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(userDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateUser(String userId, UserDTO userDTO) {
        Response response = new Response();

        try {
            User existingUser = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new MyException("user Not found"));

            //TODO create method map method in util
            if (userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
            if (userDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(userDTO.getPhoneNumber());
            if (userDTO.getName() != null) existingUser.setName(userDTO.getName());
            if (userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            userRepository.save(existingUser);

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating user" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {

        Response response = new Response();

        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {

        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(Long userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new MyException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response changeRoleById(String userId, UserRole role) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            user.setRole(role);
            userRepository.save(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error change role user " + e.getMessage());
        }
        return response;
    }
}
