package ru.taskManagement.dto.mapper;

import ru.taskManagement.dto.CommentDto;
import ru.taskManagement.dto.TaskDto;
import ru.taskManagement.dto.TaskResponseDto;
import ru.taskManagement.enumeration.Priority;
import ru.taskManagement.enumeration.Status;
import ru.taskManagement.model.Task;
import ru.taskManagement.model.User;

import java.util.List;

public class TaskMapper {

    public static Task toEntity(TaskDto taskDto, User author, User executor){
        if (taskDto == null) return null;

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus() == null? Status.NEW : taskDto.getStatus());
        task.setPriority(taskDto.getPriority() == null? Priority.NORMAL : taskDto.getPriority());
        task.setAuthor(author);
        task.setExecutor(executor);

        return task;
    }

    public static TaskResponseDto toResponseDto(Task task, List<CommentDto> comments){
        if (task == null) return null;

        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(task.getAuthorId())
                .executor(task.getExecutorId())
                .comments(comments)
                .build();
    }

    public static TaskDto toDto(Task task){
        if (task == null) return null;

        return TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(task.getAuthorId())
                .executor(task.getExecutorId())
                .build();
    }
}
