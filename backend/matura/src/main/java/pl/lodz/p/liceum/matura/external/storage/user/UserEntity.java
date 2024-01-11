package pl.lodz.p.liceum.matura.external.storage.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.liceum.matura.domain.user.UserRole;

import java.time.ZonedDateTime;

@Entity
@Table(
//        username = "USERS",
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
    private String username;
    @Column(
            nullable = false
    )
    private String password;
    @Column(
            nullable = false
    )
    private UserRole role;
    //@ManyToOne
    private Integer createdBy;
    @Column(
            nullable = false
    )
    ZonedDateTime createdAt;

    public UserEntity(String email, String username, String password, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = UserRole.valueOf(role);
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