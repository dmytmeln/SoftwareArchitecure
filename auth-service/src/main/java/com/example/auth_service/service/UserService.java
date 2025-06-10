package com.example.auth_service.service;

import com.example.auth_service.config.db.ReadOnly;
import com.example.auth_service.domain.User;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.dto.UpdatedUserRequest;
import com.example.auth_service.dto.UserResponse;
import com.example.auth_service.exception.BadRequestException;
import com.example.auth_service.exception.NotFoundException;
import com.example.auth_service.mapper.UserMapper;
import com.example.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @ReadOnly
    public UserResponse getById(Integer id) {
        return mapper.toResponse(getExisting(id));
    }

    public UserResponse create(RegisterRequest registerRequest) {
        verifyNotExistsByEmail(registerRequest.email());
        var user = mapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return mapper.toResponse(repository.save(user));
    }

    @ReadOnly
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserResponse update(Integer id, UpdatedUserRequest updatedUserRequest) {
        var user = getExisting(id);
        verifyNotExistsByEmailOnUpdate(user, updatedUserRequest.email());
        var updatedUser = repository.save(
                user.toBuilder()
                        .email(updatedUserRequest.email())
                        .fullName(updatedUserRequest.fullName())
                        .build());
        return mapper.toResponse(updatedUser);
    }

    @ReadOnly
    private User getExisting(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void verifyNotExistsByEmailOnUpdate(User user, String email) {
        if (!user.getEmail().equals(email)) {
            verifyNotExistsByEmail(email);
        }
    }

    private void verifyNotExistsByEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists");
        }
    }

}
