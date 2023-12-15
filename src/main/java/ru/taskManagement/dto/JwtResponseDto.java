package ru.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Информация о JWT токене")
public class JwtResponseDto {

    @Schema(description = "Токен")
    private String token;
    @Schema(description = "Тип токена")
    private String type = "Bearer";
    @Schema(description = "Идентификатор пользователя")
    private Long id;
    @Schema(description = "Имя пользователя")
    private String username;
    @Schema(description = "Email пользователя")
    private String email;
    @Schema(description = "Роли пользователя")
    private List<String> roles;

    public JwtResponseDto(String token, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
