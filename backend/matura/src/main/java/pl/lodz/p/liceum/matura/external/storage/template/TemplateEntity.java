package pl.lodz.p.liceum.matura.external.storage.template;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.liceum.matura.domain.template.TaskLanguage;

import java.time.ZonedDateTime;

@Entity
@Table(
//        username = "USERS",
)
@Getter
@Setter
@NoArgsConstructor
//@EqualsAndHashCode(of = "id")
public class TemplateEntity {

    @Id
    @SequenceGenerator(
            name = "template_id_seq",
            sequenceName = "template_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "template_id_seq"
    )
    private Integer id;

    @Column(nullable = false)
    private String sourceUrl;

    @Column(nullable = false)
    private TaskLanguage taskLanguage;

    @Column(nullable = false)
    private String source;

    private Integer createdBy;

    @Column(nullable = false)
    ZonedDateTime createdAt;

    public TemplateEntity(Integer id, String sourceUrl, String taskLanguage, String source) {
        this.id = id;
        this.sourceUrl = sourceUrl;
        this.taskLanguage = TaskLanguage.valueOf(taskLanguage);
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateEntity that = (TemplateEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}