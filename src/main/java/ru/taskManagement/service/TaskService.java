package ru.taskManagement.service;

import ru.taskManagement.dto.CommentDto;
import ru.taskManagement.dto.TaskDto;
import ru.taskManagement.dto.TaskResponseDto;
import ru.taskManagement.dto.UpdatedTaskDto;
import ru.taskManagement.enumeration.Status;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto, Long userId);

    List<TaskResponseDto> findAll(Long authorId, Long executorId, int from, int size);

    TaskResponseDto findById(Long taskId);

    TaskResponseDto updateTask(Long taskId, Long userId, UpdatedTaskDto updatedTaskDto);

    void deleteTask(Long taskId, Long userId);

    CommentDto postComment(CommentDto commentDto, Long taskId, Long userId);

    void deleteComment(Long taskId, Long userId, Long commentId);

    TaskResponseDto updateTaskStatus(Long taskId, Long userId, Status status);
}
