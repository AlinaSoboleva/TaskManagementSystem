package ru.taskManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.taskManagement.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTask_Id(Long task);
}
