package ru.effectivemobile.taskmanager.entity.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Check;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class DeactivatedToken {
    @Id
    UUID id;
    @Check(constraints = "deactivated_token.keep_until > now()")
    Timestamp keepUntil;

}
