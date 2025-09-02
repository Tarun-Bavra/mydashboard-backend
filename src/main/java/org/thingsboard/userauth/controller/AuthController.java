package org.thingsboard.userauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.userauth.dto.JwtResponse;
import org.thingsboard.userauth.dto.LoginRequest;
import org.thingsboard.userauth.dto.SignupRequest;
import org.thingsboard.userauth.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        JwtResponse response = userService.registerUser(signupRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<JwtResponse> optionalResponse =
                userService.authenticate(loginRequest.getEmail(), loginRequest
                        .getPassword());

        return optionalResponse
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401)
                        .body("Invalid email or password"));
    }
}
