package ru.taskManagement.model;

import lombok.*;
import ru.taskManagement.enumeration.ERole;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Setter
@Getter
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {
    }
}
