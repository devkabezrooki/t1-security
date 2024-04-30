package com.example.t1security.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class LoginRequest {

    @Size(max = 500, message = "Имя пользователя должно быть не более 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Size(max = 255, message = "Пароль должен быть от не более 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}
