package ru.taskManagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.taskManagement.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAuthor_Id(Long authorId, Pageable pageable);

    List<Task> findAllByExecutor_Id(Long executorId, Pageable pageable);
}
