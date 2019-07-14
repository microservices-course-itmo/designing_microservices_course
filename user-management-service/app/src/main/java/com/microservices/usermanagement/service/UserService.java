package com.microservices.usermanagement.service;

import com.microservices.usermanagement.dto.CreateUserDto;
import com.microservices.usermanagement.dto.UserDto;

public interface UserService {
    UserDto getUserByLogin(String login);

    UserDto createUser(CreateUserDto createUserDto);
}
