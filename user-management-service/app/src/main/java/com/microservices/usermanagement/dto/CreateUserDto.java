package com.microservices.usermanagement.dto;

import com.microservices.usermanagement.entity.CardInfo;
import com.microservices.usermanagement.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString
public class CreateUserDto {
    private String login;

    private CardInfo cardInfo;

    public CreateUserDto(UserEntity userEntity) {
        this.login = userEntity.getLogin();
        this.cardInfo = userEntity.getCardInfo();
    }
}
