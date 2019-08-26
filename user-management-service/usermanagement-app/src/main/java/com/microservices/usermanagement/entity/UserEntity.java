package com.microservices.usermanagement.entity;

import com.microservices.usermanagement.api.dto.CardInfo;
import com.microservices.usermanagement.api.dto.UserDto;
import com.microservices.usermanagement.dto.CreateUserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;

    @Enumerated(value = EnumType.STRING)
    private CardInfo cardInfo;

    public UserEntity(CreateUserDto createUserDto) {
        Objects.requireNonNull(createUserDto);
        this.login = createUserDto.getLogin();
        this.cardInfo = createUserDto.getCardInfo();
    }


    public UserDto toUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setLogin(login);
        userDto.setCardInfo(cardInfo);
        return userDto;
    }
}
