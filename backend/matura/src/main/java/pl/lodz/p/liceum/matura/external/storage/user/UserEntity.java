package pl.lodz.p.liceum.matura.external.storage.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(
//        nickname = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_email_unique",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
//@EqualsAndHashCode(of = "id")
public class UserEntity {

    @Id
    @SequenceGenerator(
            name = "user_id_seq",
            sequenceName = "user_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_seq"
    )
    private Integer id;
    @Column(
            nullable = false
    )
    private String email;
    @Column(
            nullable = false
    )
    private String nickname;
    @Column(
            nullable = false
    )
    private String password;
    @Column(
            nullable = false
    )
    private String role;
    @Column(
            nullable = false
    )
    ZonedDateTime createdAt;

    public UserEntity(String email, String nickname, String password, Set<String> roles) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}