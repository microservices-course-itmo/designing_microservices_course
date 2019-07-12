package com.microservices.usermanagement.service;

import com.microservices.usermanagement.dto.CreateUserDto;
import com.microservices.usermanagement.entity.UserEntity;

public interface UserService {
    UserEntity getUserByLogin(String login);

    CreateUserDto createUser(CreateUserDto createUserDto);
}
