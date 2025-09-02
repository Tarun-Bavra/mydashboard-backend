package org.thingsboard.userauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thingsboard.userauth.dto.JwtResponse;
import org.thingsboard.userauth.dto.SignupRequest;
import org.thingsboard.userauth.model.Role;
import org.thingsboard.userauth.model.User;
import org.thingsboard.userauth.repository.UserRepository;
import org.thingsboard.userauth.util.JwtUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // Register new user
    public JwtResponse registerUser(SignupRequest request) {
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail());

        return JwtResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    // Authenticate existing user
    public Optional<JwtResponse> authenticate(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                String token = jwtUtils.generateToken(user.getEmail());
                return Optional.of(
                        JwtResponse.builder()
                                .token(token)
                                .email(user.getEmail())
                                .fullName(user.getFullName())
                                .build()
                );
            }
        }

        return Optional.empty();
    }
}
