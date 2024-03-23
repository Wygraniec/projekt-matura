package pl.lodz.p.liceum.matura.external.storage.submissions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.liceum.matura.domain.submission.VerificationType;
import pl.lodz.p.liceum.matura.domain.template.TaskLanguage;

import java.time.ZonedDateTime;

@Entity
@Table(

)
@Getter
@Setter
@NoArgsConstructor
public class SubmissionEntity {
    @Id
    @SequenceGenerator(
            name = "submission_id_seq",
            sequenceName = "submission_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "submission_id_seq"
    )
    private Integer id;

    @Column(name="task_id", nullable = false)
    private Integer taskId;
    @Column(name="verification", nullable = false)
    private VerificationType verification;

    @Column(name="submitted_by", nullable = false)
    private Integer submittedBy;

    @Column(name="submitted_at", nullable = false)
    private ZonedDateTime submittedAt;

    public SubmissionEntity(Integer id, Integer taskId, VerificationType verificationType) {
        this.id = id;
        this.taskId = taskId;
        this.verification = verificationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionEntity that = (SubmissionEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
