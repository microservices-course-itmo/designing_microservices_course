package com.microservices.usermanagement.controller;

import com.microservices.usermanagement.dto.CreateUserDto;
import com.microservices.usermanagement.dto.UserDto;
import com.microservices.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{login}")
    UserDto getUserByLogin(@PathVariable String login) {
        return userService.getUserByLogin(login);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }
}
