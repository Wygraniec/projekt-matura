package pl.lodz.p.liceum.matura.domain.submission;

import lombok.Getter;

@Getter
public enum VerificationType {

    FAST("FAST"),
    FULL("FULL");

    private final String value;

    VerificationType(String value) {
        this.value = value;
    }
}
