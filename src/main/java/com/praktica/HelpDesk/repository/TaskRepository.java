package com.praktica.HelpDesk.repository;

import com.praktica.HelpDesk.entity.Task;
import com.praktica.HelpDesk.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Query(value = """
            SELECT * FROM tasks
            WHERE from_user_id=:userId;
            """, nativeQuery = true)
    List<Task> findTasksByUserId(@Param("userId") Long userId);

    @Query(value = """
            SELECT * FROM tasks
            WHERE to_user_id=:userId;
            """, nativeQuery = true)
    List<Task> findTasksBySysadminsId(@Param("userId") Long userId);

    @Query(value = """
            SELECT * FROM tasks
            WHERE to_user_id=:userId AND status=:taskStatus;
            """, nativeQuery = true)
    List<Task> findTasksBySysadminsIdAndStatus(Long userId, String taskStatus);
}

