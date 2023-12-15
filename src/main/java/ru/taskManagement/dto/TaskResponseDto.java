package ru.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.taskManagement.enumeration.Priority;
import ru.taskManagement.enumeration.Status;

import java.util.List;

@Data
@Builder
@Schema(description = "Информация о задаче для ответа в контроллере")
public class TaskResponseDto {
    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Название")
    private String title;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Статус")
    private Status status;

    @Schema(description = "Приоритет")
    private Priority priority;

    @Schema(description = "Автор")
    private Long author;

    @Schema(description = "Исполнитель")
    private Long executor;

    @Schema(description = "Список комментариев")
    private List<CommentDto> comments;
}
