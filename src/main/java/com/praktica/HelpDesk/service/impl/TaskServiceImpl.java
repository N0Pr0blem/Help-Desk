package com.praktica.HelpDesk.service.impl;

import com.praktica.HelpDesk.dto.filter.TaskFilter;
import com.praktica.HelpDesk.dto.task.TaskRequestDto;
import com.praktica.HelpDesk.entity.Role;
import com.praktica.HelpDesk.entity.Task;
import com.praktica.HelpDesk.entity.TaskStatus;
import com.praktica.HelpDesk.entity.UserEntity;
import com.praktica.HelpDesk.exception.TaskException;
import com.praktica.HelpDesk.repository.TaskRepository;
import com.praktica.HelpDesk.service.MailService;
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
    private final MailService mailService;

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getAll(TaskFilter taskFilter, Pageable pageable) {
        Specification<Task> spec = buildSpecification(taskFilter);

        return taskRepository.findAll(spec, pageable).stream().toList();
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
    public Task getById(Long taskId, Principal principal) {
        Task task = getById(taskId);
        UserEntity userEntity = userService.getByEmail(principal.getName());
        if (task.getFromUser().getId().equals(userEntity.getId())) return task;
        else throw new TaskException("It's doesn't your task", "TASK_ACCESS_EXCEPTION");
    }

    @Override
    public Task create(TaskRequestDto taskRequestDto, Principal principal) {
        mailService.sendInformationForm(principal.getName(), "Ваша задача успешно создана. Ожидайте.");
        notificationAllSysadmins();

        return taskRepository.save(Task.builder()
                .createdAt(LocalDateTime.now())
                .description(taskRequestDto.getDescription())
                .fromUser(userService.getByEmail(principal.getName()))
                .status(TaskStatus.WAIT)
                .build());
    }

    @Override
    public void deleteById(Long id) {
        Task task = getById(id);
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getUsersTasks(Principal principal) {
        UserEntity userEntity = userService.getByEmail(principal.getName());

        return taskRepository.findTasksByUserId(userEntity.getId());
    }

    @Override
    public List<Task> getSysadminsTasks(Principal principal) {
        UserEntity userEntity = userService.getByEmail(principal.getName());

        return taskRepository.findTasksBySysadminsId(userEntity.getId());
    }

    @Override
    public Task takeTask(Long taskId, Principal principal) {
        UserEntity user = userService.getByEmail(principal.getName());
        Task task = getById(taskId);

        if (task.getToUser()==null) {
            task.setToUser(user);
            task.setStatus(TaskStatus.IN_PROGRESS);
            mailService.sendInformationForm(task.getFromUser().getEmail(),"Статус задачи сменился на 'В процессе выполнения'");
        }
        else throw new TaskException("This task is already in progress","TASK_TAKE_EXCEPTION");

        return taskRepository.save(task);
    }

    @Override
    public Task finishTask(Long taskId, Principal principal) {
        UserEntity user = userService.getByEmail(principal.getName());
        Task task = getById(taskId);

        if(task.getStatus().equals(TaskStatus.FINISHED)) throw new TaskException("Task already finished","TASK_STATUS_EXCEPTION");
        else if(task.getStatus().equals(TaskStatus.WAIT)) throw new TaskException("You should take task to finish it","TASK_STATUS_EXCEPTION");

        if (task.getToUser().getId().equals(user.getId())) {
            task.setStatus(TaskStatus.FINISHED);
            task.setFinishedAt(LocalDateTime.now());
            mailService.sendInformationForm(task.getFromUser().getEmail(),"Статус задачи сменился на 'Выполнена'");
        } else throw new TaskException("You can't close not your's task", "TASK_ACCESS_EXCEPTION");

        return taskRepository.save(task);
    }

    @Override
    public List<Task> getSysadminsTasks(TaskStatus taskStatus, Principal principal) {
        UserEntity userEntity = userService.getByEmail(principal.getName());

        return taskRepository.findTasksBySysadminsIdAndStatus(userEntity.getId(),taskStatus.name());
    }

    @Override
    public void setTaskToSysadmin(Long taskId, Long userId) {
        Task task = getById(taskId);
        UserEntity userEntity = userService.getById(userId);
        if(!userEntity.getRole().equals(Role.SYSADMIN)) throw new TaskException("You can set task only to sysadmin","TASK_TAKE_EXCEPTION");
        if (task.getToUser()==null) {
            task.setToUser(userEntity);
            task.setStatus(TaskStatus.IN_PROGRESS);
            mailService.sendInformationForm(task.getFromUser().getEmail(),"Статус задачи сменился на 'В процессе выполнения'");
            mailService.sendInformationForm(task.getToUser().getEmail(),"Вам назначили задачу");
        }
        else throw new TaskException("This task is already in progress","TASK_TAKE_EXCEPTION");
    }

    private void notificationAllSysadmins(){
        userService.getAll().stream()
                .filter(user -> user.getRole().equals(Role.SYSADMIN))
                .forEach(user-> mailService.sendInformationForm(user.getEmail(), "Появилась новая задача."));
    }
}
