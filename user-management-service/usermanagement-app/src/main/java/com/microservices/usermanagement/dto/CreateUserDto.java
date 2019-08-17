package com.microservices.usermanagement.dto;

import com.microservices.usermanagement.entity.CardInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CreateUserDto {

    @NotBlank
    private String login;

    @NotNull
    private CardInfo cardInfo;
}
