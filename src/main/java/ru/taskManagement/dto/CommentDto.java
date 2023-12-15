package ru.taskManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о комментарии")
public class CommentDto {

    @Schema(description = "Текст комментария")
    @NotBlank
    @Size(max = 2042)
    private String text;

    @Schema(description = "Имя автора")
    private String authorName;
}
