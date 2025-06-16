package com.praktica.HelpDesk.service.impl;

import com.praktica.HelpDesk.dto.filter.TaskFilter;
import com.praktica.HelpDesk.dto.task.TaskRequestDto;
import com.praktica.HelpDesk.entity.Task;
import com.praktica.HelpDesk.entity.TaskStatus;
import com.praktica.HelpDesk.exception.TaskException;
import com.praktica.HelpDesk.repository.TaskRepository;
import com.praktica.HelpDesk.secutiry.CustomPrincipal;
import com.praktica.HelpDesk.service.TaskService;
import com.praktica.HelpDesk.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getAll(TaskFilter taskFilter, Pageable pageable) {
        Specification<Task> spec = buildSpecification(taskFilter);

        return taskRepository.findAll(spec,pageable).stream().toList();
    }

    private Specification<Task> buildSpecification(TaskFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus().toString()));
            }
            if (filter.getCreatedAt() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAt()));
            }
            if (filter.getFinishedAt() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("finishedAt"), filter.getFinishedAt()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Task getById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException("Task doesn't exist", "NO_SUCH_TASK_EXCEPTION"));
    }

    @Override
    public Task create(TaskRequestDto taskRequestDto) {
        return taskRepository.save(Task.builder()
                .createdAt(LocalDateTime.now())
                .description(taskRequestDto.getDescription())
                .fromUser(userService.getById(taskRequestDto.getFromUserId()))
                .status(TaskStatus.WAIT)
                .build());
    }

    @Override
    public void deleteById(Long id) {
        Task task = getById(id);
        taskRepository.delete(task);
    }
}
