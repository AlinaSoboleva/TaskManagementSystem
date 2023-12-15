package ru.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.taskManagement.enumeration.Priority;
import ru.taskManagement.enumeration.Status;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Информация о редактируемой задаче")
public class UpdatedTaskDto {

    @Size(max = 255)
    @Schema(description = "Название")
    private String title;

    @Size(max = 512)
    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Статус")
    private Status status;

    @Schema(description = "Приоритет")
    private Priority priority;

    @Schema(description = "Исполнитель")
    private Long executor;
}
