package com.example.practice5.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class AuthDTO {
    @NotBlank(message = "Почта (имя пользователя) не может быть пустой")
    private String username;
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
