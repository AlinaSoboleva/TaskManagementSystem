package ru.taskManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.taskManagement.dto.CommentDto;
import ru.taskManagement.dto.TaskDto;
import ru.taskManagement.dto.TaskResponseDto;
import ru.taskManagement.dto.mapper.CommentMapper;
import ru.taskManagement.dto.mapper.TaskMapper;
import ru.taskManagement.enumeration.Priority;
import ru.taskManagement.enumeration.Status;
import ru.taskManagement.exceptions.EntityNotFoundException;
import ru.taskManagement.model.Comment;
import ru.taskManagement.model.Task;
import ru.taskManagement.model.User;
import ru.taskManagement.provider.GetProvider;
import ru.taskManagement.repository.CommentRepository;
import ru.taskManagement.repository.TaskRepository;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private GetProvider provider;

    private User testUser;
    private TaskDto expectedTask;

    private TaskResponseDto taskResponseDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("username");

        expectedTask = TaskDto.builder()
                .title("TITLE")
                .description("DESC")
                .author(testUser.getId())
                .build();

        taskResponseDto = TaskResponseDto.builder()
                .title("TITLE")
                .description("DESC")
                .comments(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Создание задачи без указания статуса и приоритета")
    void createTask_WithoutStatusAndPriority() {
        when(provider.getUser(anyLong())).thenReturn(testUser);
        when(provider.getExecutor(any())).thenReturn(null);
        when(taskRepository.save(any(Task.class))).thenReturn(TaskMapper.toEntity(expectedTask, testUser, null));

        TaskDto actualTask = taskService.createTask(expectedTask, anyLong());

        expectedTask.setStatus(Status.NEW);
        expectedTask.setPriority(Priority.NORMAL);
        expectedTask.setExecutor(0L);

        assertThat(expectedTask, equalTo(actualTask));

        InOrder inOrder = Mockito.inOrder(provider);
        inOrder.verify(provider, times(1)).getUser(anyLong());
        inOrder.verify(provider, times(1)).getExecutor(any());
    }

    @Test
    @DisplayName("Создание задачи если автор не найден")
    void createTask_whenAuthorNotFound() {
        when(provider.getUser(anyLong())).thenThrow(EntityNotFoundException.class);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.createTask(expectedTask, testUser.getId()));

        assertThat(EntityNotFoundException.class, equalTo(exception.getClass()));

        InOrder inOrder = Mockito.inOrder(provider);
        inOrder.verify(provider, times(1)).getUser(anyLong());
        inOrder.verify(provider, never()).getExecutor(any());
    }

    @Test
    @DisplayName("Получение всех задач по автору")
    void findAll_IfAuthorIdIs1() {
        Task task = new Task();
        task.setId(1L);

        when(provider.getUser(anyLong())).thenReturn(testUser);
        when(taskRepository.findAllByAuthor_Id(anyLong(), any(Pageable.class))).thenReturn(List.of(task));
        when(provider.getCommentDtoList(anyLong())).thenReturn(Collections.emptyList());

        List<TaskResponseDto> actual = taskService.findAll(1L, 0L, 0, 10);

        assertThat(actual.size(), equalTo(1));
        InOrder inOrder = Mockito.inOrder(provider);
        inOrder.verify(provider, times(1)).getUser(anyLong());
        inOrder.verify(provider, never()).getExecutor(any());
        inOrder.verify(provider, times(1)).getCommentDtoList(anyLong());
    }

    @Test
    @DisplayName("Получение всех задач по исполнителю")
    void findAll_IfExecutorIdIs1() {
        Task task = new Task();
        task.setId(1L);

        when(provider.getExecutor(anyLong())).thenReturn(testUser);
        when(taskRepository.findAllByExecutor_Id(anyLong(), any(Pageable.class))).thenReturn(List.of(task));
        when(provider.getCommentDtoList(anyLong())).thenReturn(Collections.emptyList());

        List<TaskResponseDto> actual = taskService.findAll(0l, 1L, 0, 10);

        assertThat(actual.size(), equalTo(1));
        InOrder inOrder = Mockito.inOrder(provider);
        inOrder.verify(provider, times(1)).getExecutor(anyLong());
        inOrder.verify(provider, never()).getUser(any());
        inOrder.verify(provider, times(1)).getCommentDtoList(anyLong());
    }

    @Test
    void findById() {
        Task task = new Task();
        task.setId(1L);
        when(provider.getTask(anyLong())).thenReturn(task);
        when(provider.getCommentDtoList(anyLong())).thenReturn(Collections.emptyList());

        TaskResponseDto actual = taskService.findById(anyLong());

        assertThat(actual.getId(), equalTo(task.getId()));
    }

    @Test
    @DisplayName("Добавление комментария")
    void postComment() {
        Task task = new Task();
        task.setId(1L);
        CommentDto commentDto = CommentDto.builder()
                .text("comment")
                .authorName(testUser.getUsername())
                .build();
        Comment comment = CommentMapper.toEntity(commentDto, testUser, task);

        when(provider.getUser(anyLong())).thenReturn(testUser);
        when(provider.getTask(anyLong())).thenReturn(task);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto actual = taskService.postComment(commentDto, testUser.getId(), task.getId());

        assertThat(actual.getText(), equalTo(commentDto.getText()));
    }
}