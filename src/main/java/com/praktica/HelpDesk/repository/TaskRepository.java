package com.praktica.HelpDesk.repository;

import com.praktica.HelpDesk.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
