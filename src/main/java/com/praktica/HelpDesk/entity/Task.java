package com.praktica.HelpDesk.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Long from_user_id;
    private Long to_user_id;
    private LocalDateTime created_at;
    private LocalDateTime finished_at;
    private String status;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getFrom_user_id() {
        return from_user_id;
    }
    public void setFrom_user_id(Long from_user_id) {
        this.from_user_id = from_user_id;
    }
    public Long getTo_user_id() {
        return to_user_id;
    }
    public void setTo_user_id(Long to_user_id) {
        this.to_user_id = to_user_id;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public LocalDateTime getFinished_at() {
        return finished_at;
    }
    public void setFinished_at(LocalDateTime finished_at) {
        this.finished_at = finished_at;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
