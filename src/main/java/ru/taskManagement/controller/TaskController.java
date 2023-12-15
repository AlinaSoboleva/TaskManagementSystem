package ru.taskManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.taskManagement.dto.CommentDto;
import ru.taskManagement.dto.TaskDto;
import ru.taskManagement.dto.TaskResponseDto;
import ru.taskManagement.dto.UpdatedTaskDto;
import ru.taskManagement.enumeration.Status;
import ru.taskManagement.service.TaskService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.taskManagement.util.RequestHeaders.X_SHARER_USER_ID;

@RestController
@Tag(
        name = "Задачи",
        description = "Методы для упраления задачами"
)
@RequestMapping("/task")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Создание задачи пользователем",
            description = "Возвращает созданную задачу"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@RequestBody @Valid TaskDto taskDto,
                              @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Создание задачи {} пользователем с id: {}", taskDto, userId);
        return taskService.createTask(taskDto, userId);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Получение списка всех задач с возможностью фильтрации по автору или исполнителю",
            description = "Возвращает список задач с комментариями"
    )
    @GetMapping
    public List<TaskResponseDto> findAllTasks(@RequestParam(required = false, defaultValue = "0")@PositiveOrZero  long authorId,
                                              @RequestParam(required = false, defaultValue = "0")@PositiveOrZero long executorId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получение всех задач");
        return taskService.findAll(authorId, executorId, from, size);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Получение задачи по id",
            description = "Возвращает задачу с комментариями"
    )
    @GetMapping("/{taskId}")
    public TaskResponseDto findTaskById(@PathVariable Long taskId) {
        log.info("Получение задачи с id {}", taskId);
        return taskService.findById(taskId);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Редактирование задачи автором",
            description = "Возвращает отредактированную задачу"
    )
    @PatchMapping("/{taskId}")
    public TaskResponseDto updateTask(@PathVariable Long taskId,
                                      @RequestHeader(X_SHARER_USER_ID) Long userId,
                                      @RequestBody @Valid UpdatedTaskDto updatedTaskDto) {
        log.info("Изменение задачи id {} пользователем с id {}", taskId, userId);
        return taskService.updateTask(taskId, userId, updatedTaskDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Измненение статуса задачи автором или исполнителем",
            description = "Возвращает измененную задачу"
    )
    @PatchMapping("/{taskId}/status")
    public TaskResponseDto updateTaskStatus(@PathVariable Long taskId,
                                            @RequestHeader(X_SHARER_USER_ID) Long userId,
                                            @RequestParam(defaultValue = "NEW") Status status) {
        log.info("Изменение статуса задачи пользоваетля с id {}", userId);
        return taskService.updateTaskStatus(taskId, userId, status);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Удаление задачи по id",
            description = "Ничего не возвращает"
    )
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId,
                           @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Удаление задачи с id {}", taskId);
        taskService.deleteTask(taskId, userId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{taskId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@Valid @RequestBody CommentDto commentDto,
                                  @PathVariable Long taskId,
                                  @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return taskService.postComment(commentDto, taskId, userId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{taskId}/comment/{commentId}")
    public void deleteComment(@PathVariable Long taskId,
                              @PathVariable Long commentId,
                              @RequestHeader(X_SHARER_USER_ID) Long userId) {
        taskService.deleteComment(taskId, userId, commentId);
    }
}
