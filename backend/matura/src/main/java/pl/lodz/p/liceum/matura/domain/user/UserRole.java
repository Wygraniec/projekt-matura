package pl.lodz.p.liceum.matura.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN ("ADMIN"),
    STUDENT("STUDENT"),
    INSTRUCTOR("INSTRUCTOR");


    private final String value;

    UserRole(String value) {
        this.value = value;
    }

}
