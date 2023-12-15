package ru.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Сообщение для ответа")
public class MessageResponseDto {

    @Schema(description = "Текст сообщения")
    private String message;
}
