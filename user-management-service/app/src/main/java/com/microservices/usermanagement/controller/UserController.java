package com.microservices.usermanagement.controller;

import com.microservices.usermanagement.dto.UserDto;
import com.microservices.usermanagement.entity.UserEntity;
import com.microservices.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{login}")
    UserEntity getUserByLogin(@PathVariable String login) {
        return userService.getUserByLogin(login);
    }

    @PostMapping(value = "userInfo")
    UserEntity addUserToDataBase(@Valid @RequestBody UserDto userDto) {
        return userService.addUserToDataBase(userDto);
    }

    @PostMapping(value = "userInf")
    UserEntity addUserToDataBase(@RequestParam String login, @RequestParam String status) {
        UserDto userDto = new UserDto();
        userDto.setLogin(login);
        userDto.setStatusOfBankCard(status);
        return userService.addUserToDataBase(userDto);
    }
}
