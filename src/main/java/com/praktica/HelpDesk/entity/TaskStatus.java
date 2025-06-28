package com.praktica.HelpDesk.entity;

public enum TaskStatus {
    WAIT, IN_PROGRESS, FINISHED;

    public static TaskStatus fromString(String taskStatus) {
        for(TaskStatus s : TaskStatus.values()){
            if(s.name().equals(taskStatus.toUpperCase())) return s;
        }
        return TaskStatus.WAIT;
    }
}
