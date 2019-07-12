package com.microservices.usermanagement.service.impl;

import com.microservices.usermanagement.dto.UserDto;
import com.microservices.usermanagement.entity.UserEntity;
import com.microservices.usermanagement.repository.UserRepository;
import com.microservices.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getUserByLogin(String login) {
        Objects.requireNonNull(login);
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("There is no such user in database for login: " + login));
    }

    @Override
    public UserEntity createUser(UserDto userDto) {
        return userRepository.save(new UserEntity(userDto));
    }
}
