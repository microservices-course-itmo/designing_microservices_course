package com.microservices.usermanagement.api.service;

import com.microservices.usermanagement.api.dto.UserDto;

/**
 * Interface for accessing user service
 */
public interface UserServiceApi {

    /**
     * Returns saved user by provided login
     *
     * @throws IllegalArgumentException if specified used is not found
     * @throws NullPointerException     if provided login is null
     */
    UserDto getUserByLogin(String login);
}
