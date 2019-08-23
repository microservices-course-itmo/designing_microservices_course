package com.microservices.usermanagement.service.impl;

import com.microservices.usermanagement.api.dto.UserDto;
import com.microservices.usermanagement.dto.CreateUserDto;
import com.microservices.usermanagement.entity.UserEntity;
import com.microservices.usermanagement.repository.UserRepository;
import com.microservices.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserByLogin(String login) {
        Objects.requireNonNull(login);
        logger.info("Find user with login: {}", login);
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("There is no such user in database for login: " + login)).toUserDto();
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        if (userRepository.existsByLogin(createUserDto.getLogin())) {
            throw new IllegalArgumentException("User already exists, login: " + createUserDto.getLogin());
        }
        logger.info("Creating user: {} ", createUserDto);
        UserEntity createdUser = userRepository.save(new UserEntity(createUserDto));
        logger.info("Created new user: {}", createdUser);
        return createdUser.toUserDto();
    }
}
