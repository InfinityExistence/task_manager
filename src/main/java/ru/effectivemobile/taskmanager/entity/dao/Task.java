package ru.effectivemobile.taskmanager.entity.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.effectivemobile.taskmanager.entity.Priority;
import ru.effectivemobile.taskmanager.entity.Status;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Task {
    @Id
    private UUID id;
    private String title;
    private String description;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actor_id")
    private User actor;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "task")
    private List<Comment> comments;


}
