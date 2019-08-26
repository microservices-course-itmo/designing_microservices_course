package com.microservices.usermanagement.service;

import com.microservices.usermanagement.api.dto.UserDto;
import com.microservices.usermanagement.dto.CreateUserDto;

public interface UserService {
    UserDto getUserByLogin(String login);

    UserDto createUser(CreateUserDto createUserDto);
}
