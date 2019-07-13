package com.microservices.usermanagement.dto;

import com.microservices.usermanagement.entity.CardInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateUserDto {
    private String login;

    private CardInfo cardInfo;
}
