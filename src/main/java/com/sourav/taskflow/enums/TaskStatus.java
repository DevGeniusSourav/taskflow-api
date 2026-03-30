package com.sourav.taskflow.enums;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE;

    public boolean canTransitionTo(TaskStatus status) {
        switch (this) {
            case TODO:
                return status == IN_PROGRESS;
            case IN_PROGRESS:
                return status == DONE;
            case DONE:
                return false;
        }
        return false;
    }
}
