package ru.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Информация о пользователе")
public class UserDto {
    @Schema(description = "Идентификатор")
    private Long id;

    @Email
    @Schema(description = "Email")
    private String email;

    @NotBlank
    @Schema(description = "Имя пользователя")
    private String username;

    @NotBlank
    @Schema(description = "Пароль")
    private String password;

    @Schema(description = "Список ролей")
    private Set<String> roles;
}
