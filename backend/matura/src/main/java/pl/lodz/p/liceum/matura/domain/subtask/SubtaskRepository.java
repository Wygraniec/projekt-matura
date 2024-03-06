package pl.lodz.p.liceum.matura.domain.subtask;

import java.util.Optional;

public interface SubtaskRepository {
    Subtask save(Subtask task);

    void update(Subtask task);

    void remove(Integer id);


    Optional<Subtask> findById(Integer id);
}
