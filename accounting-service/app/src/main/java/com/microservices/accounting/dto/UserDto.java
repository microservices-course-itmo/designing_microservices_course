package com.microservices.accounting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {

    @NotNull
    private String login;

    @NotNull
    private CardInfo cardInfo;
}
