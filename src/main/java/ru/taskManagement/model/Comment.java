package ru.taskManagement.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @Getter(AccessLevel.NONE)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @Getter(AccessLevel.NONE)
    private User author;

    public Long getAuthorId() {
        return author.getId();
    }

    public String getAuthorName() {
        return author.getUsername();
    }

    public Long getTaskId() {
        return task.getId();
    }
}
