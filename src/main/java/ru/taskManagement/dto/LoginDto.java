package ru.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Schema(description = "LoginDto для авторизации")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @Schema(description = "Email (логин)")
    @Email
    private String email;
    @Schema(description = "Пароль")
    @NotBlank
    private String password;

}