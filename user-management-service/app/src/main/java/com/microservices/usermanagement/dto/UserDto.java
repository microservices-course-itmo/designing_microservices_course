package com.microservices.usermanagement.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private String login;
    private String cardInfo;
}
