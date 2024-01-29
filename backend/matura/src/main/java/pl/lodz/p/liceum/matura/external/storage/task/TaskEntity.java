package pl.lodz.p.liceum.matura.external.storage.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.liceum.matura.domain.task.TaskState;
import pl.lodz.p.liceum.matura.external.storage.template.TemplateEntity;

import java.time.ZonedDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class TaskEntity {
    @Id
    @SequenceGenerator(
            name = "task_id_seq",
            sequenceName = "task_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_id_seq"
    )
    private Integer id;

    @Column(name="user_id", nullable = false)
    private Integer userId;

    @Column(name="template_id", nullable = false)
    private Integer templateId;

    @Column(name="workspace_url", nullable = false)
    private String workspaceUrl;

    @Column(name="state", nullable = false)
    private TaskState state;

    @Column(name="created_by", nullable = false)
    private Integer createdBy;

    @Column(name="created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
