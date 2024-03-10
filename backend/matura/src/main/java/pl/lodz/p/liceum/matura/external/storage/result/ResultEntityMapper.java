package pl.lodz.p.liceum.matura.external.storage.result;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.result.Result;

@Mapper(componentModel = "spring")
public interface ResultEntityMapper {
    ResultEntity toEntity(Result domain);
    Result toDomain(ResultEntity entity);
}
