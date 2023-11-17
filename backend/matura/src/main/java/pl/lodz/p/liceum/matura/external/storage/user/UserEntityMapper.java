package pl.lodz.p.liceum.matura.external.storage.user;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.user.User;


@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntity toEntity(User domain);

    User toDomain(UserEntity entity);

}
