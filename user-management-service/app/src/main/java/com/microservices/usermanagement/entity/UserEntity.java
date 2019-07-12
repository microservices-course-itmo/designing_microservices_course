package com.microservices.usermanagement.entity;

import com.microservices.usermanagement.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;

    @Column(name = "card_info")
    @Enumerated(value = EnumType.STRING)
    private CardInfo cardInfo;

    public UserEntity(UserDto userDto) {
        this.login = userDto.getLogin();
        this.cardInfo = userDto.getCardInfo();
    }
}
