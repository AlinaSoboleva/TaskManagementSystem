package ru.taskManagement.provider;

import ru.taskManagement.dto.CommentDto;
import ru.taskManagement.dto.mapper.CommentMapper;
import ru.taskManagement.enumeration.ERole;
import ru.taskManagement.model.Comment;
import ru.taskManagement.model.Role;
import ru.taskManagement.model.Task;
import ru.taskManagement.model.User;
import ru.taskManagement.repository.CommentRepository;
import ru.taskManagement.repository.RoleRepository;
import ru.taskManagement.repository.TaskRepository;
import ru.taskManagement.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.taskManagement.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProvider {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final CommentRepository commentRepository;

    private final RoleRepository roleRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId)));
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Комментарий с id %d не найден", commentId)));
    }

    public User getExecutor(Long userId) {
        if (userId == null) return null;
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId)));
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Задача с id %d не найдена", taskId)));
    }

    public Role getRileByName(ERole eRole){
        return roleRepository
                .findByName(eRole)
                .orElseThrow(() -> new EntityNotFoundException("Ошибка: роль USER не найдена"));
    }

    public List<CommentDto> getCommentDtoList(Long taskId){
        return commentRepository.findAllByTask_Id(taskId)
                .stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }
}
