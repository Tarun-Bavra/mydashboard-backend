package org.thingsboard.userauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.userauth.model.User;
import org.thingsboard.userauth.dto.UserResponse;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<UserResponse>
    getProfile(@AuthenticationPrincipal User user) {
        UserResponse response = new UserResponse(user.getId(),
                user.getFullName(), user.getEmail());
        return ResponseEntity.ok(response);
    }
}
