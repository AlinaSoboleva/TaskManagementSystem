package ru.taskManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.taskManagement.dto.CommentDto;
import ru.taskManagement.dto.TaskDto;
import ru.taskManagement.dto.TaskResponseDto;
import ru.taskManagement.dto.UpdatedTaskDto;
import ru.taskManagement.enumeration.Priority;
import ru.taskManagement.enumeration.Status;
import ru.taskManagement.service.TaskService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.taskManagement.util.RequestHeaders.X_SHARER_USER_ID;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        taskDto = TaskDto.builder()
                .title("task")
                .description("newTask")
                .author(1L)
                .status(Status.NEW)
                .priority(Priority.NORMAL)
                .build();
    }

    @Test
    @DisplayName("Создание задачи")
    @WithMockUser
    void createTask_whenInvoked_thenResponseStatusIsCreated() throws Exception {
        Mockito.when(taskService.createTask(taskDto, 1L)).thenReturn(taskDto);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("Создание задачи с пустым названием ")
    @WithMockUser
    void createTask_whenTitleIsBlank_thenResponseStatusIsBadRequest() throws Exception {
        taskDto.setTitle("");
        Mockito.when(taskService.createTask(taskDto, 1L)).thenReturn(taskDto);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(taskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание задачи с пустым описанием")
    @WithMockUser
    void createTask_whenDescriptionIsBlank_thenResponseStatusIsBadRequest() throws Exception {
        taskDto.setDescription("");
        Mockito.when(taskService.createTask(taskDto, 1L)).thenReturn(taskDto);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(taskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание задачи с null описанием")
    @WithMockUser
    void createTask_whenDescriptionIsNull_thenResponseStatusIsBadRequest() throws Exception {
        taskDto.setDescription(null);
        Mockito.when(taskService.createTask(taskDto, 1L)).thenReturn(taskDto);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(taskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание задачи с null названием")
    @WithMockUser
    void createTask_whenTitleIsNull_thenResponseStatusIsBadRequest() throws Exception {
        taskDto.setTitle(null);
        Mockito.when(taskService.createTask(taskDto, 1L)).thenReturn(taskDto);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(taskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Получение всех задач")
    @WithMockUser
    void findAllTasks_whenInvoked_thenResponseStatusIsOk() throws Exception {
        List<TaskResponseDto> taskResponseDtos = new ArrayList<>();
        Mockito.when(taskService.findAll(0L, 0L, 0, 10)).thenReturn(taskResponseDtos);

        mockMvc.perform(get("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Получение всех задач когда полe size = 0")
    @WithMockUser
    void findAllTasks_whenSizeIsZero_thenResponseStatusIsBadRequest() throws Exception {
        List<TaskResponseDto> taskResponseDtos = new ArrayList<>();
        Mockito.when(taskService.findAll(0L, 0L, 0, 0)).thenReturn(taskResponseDtos);

        mockMvc.perform(get("/task")
                        .param("size", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Получение всех задач когда полe size = -5")
    @WithMockUser
    void findAllTasks_whenSizeIsNegative_thenResponseStatusIsBadRequest() throws Exception {
        List<TaskResponseDto> taskResponseDtos = new ArrayList<>();
        Mockito.when(taskService.findAll(0L, 0L, 0, -5)).thenReturn(taskResponseDtos);

        mockMvc.perform(get("/task")
                        .param("size", "-5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Получение задачи по id")
    @WithMockUser
    void findTaskById_whenInvoked_thenResponseStatusIsOk() throws Exception {
        TaskResponseDto taskResponseDto = TaskResponseDto.builder().build();
        Mockito.when(taskService.findById(1L)).thenReturn(taskResponseDto);

        mockMvc.perform(get("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Изменение задачи")
    @WithMockUser
    void updateTask_whenInvoked_thenResponseStatusIsOk() throws Exception {
        TaskResponseDto taskResponseDto = TaskResponseDto.builder().build();
        UpdatedTaskDto updatedTaskDto = UpdatedTaskDto.builder().build();
        Mockito.when(taskService.updateTask(1L, 1L, updatedTaskDto)).thenReturn(taskResponseDto);

        mockMvc.perform(patch("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(updatedTaskDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Изменение статуса задачи")
    @WithMockUser
    void updateTaskStatus() throws Exception {
        TaskResponseDto taskResponseDto = TaskResponseDto.builder().build();
        Mockito.when(taskService.updateTaskStatus(1L, 1L, Status.NEW)).thenReturn(taskResponseDto);

        mockMvc.perform(patch("/task/1/status")
                        .param("status", "NEW")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Изменение статуса задачи")
    @WithMockUser
    void updateTaskStatus_whenStatusIsNotValid() throws Exception {
        TaskResponseDto taskResponseDto = TaskResponseDto.builder().build();
        Mockito.when(taskService.updateTaskStatus(1L, 1L, Status.NEW)).thenReturn(taskResponseDto);

        mockMvc.perform(patch("/task/1/status")
                        .param("status", "XXX")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Добавление комментария")
    @WithMockUser
    void postComment() throws Exception{
        CommentDto commentDto = CommentDto.builder().text("Комментарий").build();
        Mockito.when(taskService.postComment(commentDto, 1L, 1L)).thenReturn(commentDto);

        mockMvc.perform(post("/task/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto))
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}