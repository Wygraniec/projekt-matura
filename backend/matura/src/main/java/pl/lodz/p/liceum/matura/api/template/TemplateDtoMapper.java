package pl.lodz.p.liceum.matura.api.template;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.template.Template;

@Mapper(componentModel = "spring")
public interface TemplateDtoMapper {
    TemplateDto toDto(Template domain);
    Template toDomain(TemplateDto dto);
}
