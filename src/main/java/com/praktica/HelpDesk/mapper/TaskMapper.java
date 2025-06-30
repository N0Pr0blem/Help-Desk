package com.praktica.HelpDesk.mapper;


import com.praktica.HelpDesk.dto.task.TaskResponseDto;
import com.praktica.HelpDesk.entity.Task;
import com.praktica.HelpDesk.mapper.base.Mappable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskResponseDto> {
}
