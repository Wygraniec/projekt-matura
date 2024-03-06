package pl.lodz.p.liceum.matura.domain.subtask;

import lombok.RequiredArgsConstructor;
import pl.lodz.p.liceum.matura.domain.task.TaskNotFoundException;

@RequiredArgsConstructor
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;

    public Subtask save(Subtask subtask) {
        return subtaskRepository.save(subtask);
    }

    public void update(Subtask subtask) {
        subtaskRepository.update(subtask);
    }

    public void removeById(Integer id) {
        subtaskRepository.remove(id);
    }

    public Subtask findById(Integer id) {
        return subtaskRepository
                .findById(id)
                .orElseThrow(TaskNotFoundException::new);
    }
}
