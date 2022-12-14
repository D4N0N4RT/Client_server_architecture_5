package com.example.practice5.dto;

import com.example.practice5.model.Category;
import com.example.practice5.model.Post;
import com.example.practice5.model.User;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
public class CreatePostDTO {
    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 100, message = "Название не может быть длинее 100 символов")
    private String title;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Цена не может отсутствовать")
    @Min(value = 1, message = "Цена должна быть не меньше 1")
    private double price;
    @NotNull(message = "Объявление не может быть без категории")
    private Category category;

    public Post toPost(User user) {
        return Post.builder().title(title).description(description).price(price)
                .promotion(0).sold(false).category(category).user(user)
                .rating(user.getRating()).postingDate(LocalDate.now()).build();
    }
}
