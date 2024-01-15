package pl.lodz.p.liceum.matura.external.storage.template;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.template.Template;

@Mapper(componentModel = "spring")
public interface TemplateEntityMapper {

    TemplateEntity toEntity(Template domain);

    Template toDomain(TemplateEntity entity);

}
