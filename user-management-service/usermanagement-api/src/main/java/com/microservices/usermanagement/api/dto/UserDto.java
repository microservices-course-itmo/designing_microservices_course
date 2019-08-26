package com.microservices.usermanagement.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private int id;

    private String login;

    private CardInfo cardInfo;
}
