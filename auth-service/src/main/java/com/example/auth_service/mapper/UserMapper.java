package com.example.auth_service.mapper;

import com.example.auth_service.domain.User;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", constant = "ROLE_USER")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest registerRequest);

    UserResponse toResponse(User user);

}
