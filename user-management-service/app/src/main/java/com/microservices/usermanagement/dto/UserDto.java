package com.microservices.usermanagement.dto;

import com.microservices.usermanagement.entity.CardInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString
public class UserDto {
    private String login;

    @Enumerated(value = EnumType.STRING)
    private CardInfo cardInfo;
}
