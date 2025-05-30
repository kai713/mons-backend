package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.UserDto;
import com.kairgaliyev.backendonlineshop.service.intreface.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;

    @GetMapping("/profile")
    public UserDto getProfile(HttpServletRequest request) {
        Long userId = (long) request.getAttribute("userId");
        return userService.getCurrentUser(userId);
    }
//
//    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Response> getAllUsers() {
//        Response response = iUserService.getAllUsers();
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//
//    @GetMapping("/get-by-id/{userId}")
//    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId) {
//        Response response = iUserService.getUserById(userId);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//
//    @PutMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Response> updateUser(@RequestBody UserDTO userDTO) {
//        Response response = iUserService.updateUser(userDTO.getId().toString(), userDTO);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//
//    @DeleteMapping("/delete/{userId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Response> deleteUSer(@PathVariable("userId") String userId) {
//        Response response = iUserService.deleteUser(userId);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//
//    @GetMapping("/get-logged-in-profile-info")
//    public ResponseEntity<Response> getLoggedInUserProfile(@RequestAttribute("userId") Long userId) {
//        log.info("Get logged in user profile info for user id {}", userId);
//        Response response = iUserService.getMyInfo(userId);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//
//    @PutMapping("/changeRole/{userId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Response> changeUserRole(@PathVariable("userId") String userId, @RequestBody StatusRoleRequest request) {
//        UserRole userRole = request.getUserRoleEnum();
//        Response response = iUserService.changeRoleById(userId, userRole);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
}
