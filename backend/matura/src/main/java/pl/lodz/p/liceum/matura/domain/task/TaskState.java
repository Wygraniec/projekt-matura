package pl.lodz.p.liceum.matura.domain.task;

import lombok.Getter;

@Getter
public enum TaskState {

    CREATED("CREATED"),
    PROCESSING("PROCESSING"),
    FINISHED("FINISHED");


    private final String value;

    TaskState(String value) {
        this.value = value;
    }

}
