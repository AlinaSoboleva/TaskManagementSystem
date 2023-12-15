package ru.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Информация о пользователе")
public class UserResponseDto {
    @Schema(description = "Идентификатор")
    private Long id;
    @Schema(description = "Email")
    private String email;
    @Schema(description = "Имя пользователя")
    private String username;
}
