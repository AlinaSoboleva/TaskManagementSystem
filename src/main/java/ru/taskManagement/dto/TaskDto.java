package ru.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.taskManagement.enumeration.Priority;
import ru.taskManagement.enumeration.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Schema(description = "Информация о задаче для CRUD операций")
public class TaskDto {

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Название задачи")
    private String title;
    @NotBlank
    @Size(max = 512)
    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Статус")
    private Status status;

    @Schema(description = "Приоритет")
    private Priority priority;

    @Schema(description = "Исполнитель")
    private Long executor;

    @Schema(description = "Автор")
    private Long author;
}
