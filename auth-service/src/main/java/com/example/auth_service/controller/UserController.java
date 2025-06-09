package com.example.auth_service.controller;

import com.example.auth_service.dto.UpdatedUserRequest;
import com.example.auth_service.dto.UserResponse;
import com.example.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserResponse getUser(@RequestHeader("X-User-Id") Integer id) {
        return userService.getById(id);
    }

    @GetMapping("/email")
    public String getUserEmail(@RequestHeader("X-User-Id") Integer id) {
        return userService.getById(id)
                .email();
    }

    @PutMapping
    public UserResponse update(@RequestHeader("X-User-Id") Integer id,
                               @RequestBody UpdatedUserRequest updatedUserRequest
    ) {
        return userService.update(id, updatedUserRequest);
    }

}
