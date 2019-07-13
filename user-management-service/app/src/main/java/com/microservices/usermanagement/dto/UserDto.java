package com.microservices.usermanagement.dto;

import com.microservices.usermanagement.entity.CardInfo;
import com.microservices.usermanagement.entity.UserEntity;
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

    public UserDto(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.login = userEntity.getLogin();
        this.cardInfo = userEntity.getCardInfo();
    }
}
