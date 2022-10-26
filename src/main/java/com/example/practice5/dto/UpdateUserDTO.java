package com.example.practice5.dto;

import lombok.Getter;

import javax.validation.constraints.Size;
@Getter
public class UpdateUserDTO implements IUserDTO {
    @Size(min = 1, max = 255, message = "Пароль не может отсутствовать или превышать 255 символов")
    private String password;
    @Size(min = 1, max = 50, message = "Имя не может отсутствовать или превышать 50 символов")
    private String name;
    @Size(min = 1,max = 75, message = "Фамилия не может отсутствовать или превышать 75 символов")
    private String surname;
    @Size(min = 10, max = 10, message = "Номер телефона должен содержать 10 цифр")
    private String phone;
}
