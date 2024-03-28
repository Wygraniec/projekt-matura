package pl.lodz.p.liceum.matura.api.template;

import java.util.List;

public record PageTemplateDto(
        List<TemplateDto> templates,
        Integer currentPage,
        Integer totalPages,
        Long totalElements
) {
}
