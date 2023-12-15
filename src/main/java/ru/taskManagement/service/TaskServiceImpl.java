package ru.taskManagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taskManagement.dto.CommentDto;
import ru.taskManagement.dto.TaskDto;
import ru.taskManagement.dto.TaskResponseDto;
import ru.taskManagement.dto.UpdatedTaskDto;
import ru.taskManagement.dto.mapper.CommentMapper;
import ru.taskManagement.dto.mapper.TaskMapper;
import ru.taskManagement.enumeration.Status;
import ru.taskManagement.exceptions.ConflictException;
import ru.taskManagement.model.Comment;
import ru.taskManagement.model.Task;
import ru.taskManagement.model.User;
import ru.taskManagement.provider.GetProvider;
import ru.taskManagement.repository.CommentRepository;
import ru.taskManagement.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final CommentRepository commentRepository;

    private final GetProvider provider;

    @Override
    public TaskDto createTask(TaskDto taskDto, Long userId) {
        User author = provider.getUser(userId);
        User executor = provider.getExecutor(taskDto.getExecutor());

        Task task = TaskMapper.toEntity(taskDto, author, executor);
        taskRepository.save(task);

        return TaskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> findAll(Long authorId, Long executorId, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        List<Task> tasks;
        if (authorId != 0 && executorId == 0) {
            provider.getUser(authorId);
            tasks = taskRepository.findAllByAuthor_Id(authorId, page);
        } else if (executorId != 0 && authorId == 0) {
            provider.getExecutor(executorId);
            tasks = taskRepository.findAllByExecutor_Id(executorId, page);
        } else {
            tasks = taskRepository.findAll(page).getContent();
        }
        List<TaskResponseDto> response = new ArrayList<>();
        for (Task task : tasks) {
            List<CommentDto> comments = provider.getCommentDtoList(task.getId());
            response.add(TaskMapper.toResponseDto(task, comments));
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto findById(Long taskId) {
        Task task = provider.getTask(taskId);
        List<CommentDto> comments = provider.getCommentDtoList(taskId);

        return TaskMapper.toResponseDto(task, comments);
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, Long userId, UpdatedTaskDto updatedTaskDto) {
        provider.getUser(userId);
        User executor = provider.getExecutor(updatedTaskDto.getExecutor());

        Task task = provider.getTask(taskId);
        checkTaskAuthor(task, userId);

        task.setExecutor(executor);
        task.setTitle(updatedTaskDto.getTitle() == null || updatedTaskDto.getTitle().isBlank() ? task.getTitle() : updatedTaskDto.getTitle());
        task.setDescription(updatedTaskDto.getDescription() == null || updatedTaskDto.getDescription().isBlank() ? task.getDescription() : updatedTaskDto.getDescription());
        task.setStatus(updatedTaskDto.getStatus() == null ? task.getStatus() : updatedTaskDto.getStatus());
        task.setPriority(updatedTaskDto.getPriority() == null ? task.getPriority() : updatedTaskDto.getPriority());

        List<CommentDto> comments = provider.getCommentDtoList(taskId);

        return TaskMapper.toResponseDto(task, comments);
    }

    @Override
    public TaskResponseDto updateTaskStatus(Long taskId, Long userId, Status status) {
        provider.getUser(userId);
        Task task = provider.getTask(taskId);
        checkTaskForStatusUpdate(task, userId);

        task.setStatus(status);

        List<CommentDto> comments = provider.getCommentDtoList(taskId);

        return TaskMapper.toResponseDto(task, comments);
    }

    @Override
    public void deleteTask(Long taskId, Long userId) {
        Task task = provider.getTask(taskId);
        provider.getUser(userId);
        checkTaskAuthor(task, userId);
        taskRepository.delete(task);
    }

    @Override
    public CommentDto postComment(CommentDto commentDto, Long userId, Long taskId) {
        Task task = provider.getTask(taskId);
        User user = provider.getUser(userId);
        Comment comment = CommentMapper.toEntity(commentDto, user, task);
        commentRepository.save(comment);
        return CommentMapper.toDto(comment);
    }

    @Override
    public void deleteComment(Long taskId, Long userId, Long commentId) {
        provider.getUser(userId);
        Task task = provider.getTask(taskId);
        Comment comment = provider.getComment(commentId);

        checkRightToDeleteComment(comment, task, userId);

        commentRepository.delete(comment);
    }

    private void checkRightToDeleteComment(Comment comment, Task task, Long userId) {
        if (comment.getAuthorId() != userId || task.getAuthorId() != userId)
            throw new ConflictException(String.format("Пользователь с id %d не имеет права удалять комментарий с id %d", userId, comment.getId()));
    }

    private void checkTaskAuthor(Task task, Long authorId) {
        if (task.getAuthorId() != authorId)
            throw new ConflictException(String.format("Пользователь с id %d не имеет права редактировать задачу с id %d", authorId, task.getId()));
    }

    private void checkTaskForStatusUpdate(Task task, Long userId) {
        if (task.getExecutorId() != userId && task.getAuthorId() != userId)
            throw new ConflictException(String.format("Пользователь с id %d не имеет права менять статус задачи с id %d", userId, task.getId()));
    }
}
