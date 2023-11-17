package pl.lodz.p.liceum.matura.api.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.p.liceum.matura.domain.user.User;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(target="password", constant = "######")
    UserDto toDto(User domain);

    User toDomain(UserDto dto);
}