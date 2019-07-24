package com.microservices.accounting.temporarydtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
//will be lately imported from user management service api
public class UserDto {
    @NotNull
    private Integer id;

    @NotNull
    private String login;

    @NotNull
    private CardInfo cardInfo;
}
