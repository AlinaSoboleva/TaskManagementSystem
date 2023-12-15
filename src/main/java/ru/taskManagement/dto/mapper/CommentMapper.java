package ru.taskManagement.dto.mapper;

import ru.taskManagement.dto.CommentDto;
import ru.taskManagement.model.Comment;
import ru.taskManagement.model.Task;
import ru.taskManagement.model.User;

public class CommentMapper {

    public static Comment toEntity(CommentDto commentDto, User author, Task task) {
        if (commentDto == null) return null;

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setText(commentDto.getText());
        comment.setTask(task);

        return comment;
    }

    public static CommentDto toDto(Comment comment) {
        if (comment == null) return null;

        return CommentDto.builder()
                .text(comment.getText())
                .authorName(comment.getAuthorName())
                .build();
    }
}
