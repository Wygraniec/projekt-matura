package pl.lodz.p.liceum.matura.api.template;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.lodz.p.liceum.matura.domain.template.PageTemplate;
import pl.lodz.p.liceum.matura.domain.template.Template;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PageTemplateDtoMapper {

    @Mapping(target = "templates", qualifiedByName = "toTemplateDtoList")
    PageTemplateDto toPageDto(PageTemplate domain);

    @Named("toTemplateDtoList")
    @IterableMapping(qualifiedByName = "templateToTemplateDto")
    List<TemplateDto> toListDto(List<Template> templates);

    @Named("templateToTemplateDto")
    TemplateDto toDto(Template domain);
}