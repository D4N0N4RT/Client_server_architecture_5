package com.example.practice5.dto;

import com.example.practice5.model.Role;
import com.example.practice5.model.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserDTO implements IUserDTO {
    @NotBlank(message = "Почта (имя пользователя) не может быть пустой")
    @Size(max = 50, message = "Почта не может превышать 50 символов")
    private String username;
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(max = 255, message = "Пароль не может превышать 255 символов")
    private String password;
    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 50, message = "Имя не может превышать 50 символов")
    private String name;
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(max = 75, message = "Фамилия не может превышать 75 символов")
    private String surname;
    @NotBlank(message = "Номер телефона не может отсутствовать")
    @Size(min = 10, max = 10, message = "Номер телефона должен содержать 10 цифр")
    private String phone;

    public User toUser() {
        return User.builder().username(username).password(password)
                .name(name).surname(surname).phone(phone).isActive(true)
                .rating(0).role(Role.USER).build();
    }
}
