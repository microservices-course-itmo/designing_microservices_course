package com.microservices.usermanagement.entity;

import com.microservices.usermanagement.dto.CreateUserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
}
