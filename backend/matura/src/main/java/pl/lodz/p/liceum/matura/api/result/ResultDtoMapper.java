package pl.lodz.p.liceum.matura.api.result;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.result.Result;

@Mapper(componentModel = "spring")
public interface ResultDtoMapper {
    ResultDto toDto(Result domain);
    Result toDomain(ResultDto dto);
}
