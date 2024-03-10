package pl.lodz.p.liceum.matura.domain.result;

import java.util.Optional;

public interface ResultRepository {
    Result save(Result result);

    void update(Result result);

    void remove(Integer id);


    Optional<Result> findById(Integer id);
}
