package com.microservices.usermanagement.service;

import com.microservices.usermanagement.dto.UserDto;
import com.microservices.usermanagement.entity.UserEntity;

public interface UserService {
    UserEntity getUserByLogin(String login);

    UserEntity addUserToDataBase(UserDto userDto);
}
