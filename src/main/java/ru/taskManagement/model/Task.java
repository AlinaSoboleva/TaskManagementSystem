package ru.taskManagement.model;

import lombok.*;
import ru.taskManagement.enumeration.Priority;
import ru.taskManagement.enumeration.Status;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    @Getter(AccessLevel.NONE)
    private User author;
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor")
    @Getter(AccessLevel.NONE)
    private User executor;

    public Long getAuthorId() {
        if (author == null) return 0L;
        return author.getId();
    }

    public Long getExecutorId() {
        if (executor == null) return 0L;
        return executor.getId();
    }
}
