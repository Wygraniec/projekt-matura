package pl.lodz.p.liceum.matura.api.template;

import java.time.ZonedDateTime;

public record TemplateDto(
        Integer id,
        String sourceUrl,
        String taskLanguage,
        String source,
        Integer createdBy,
        ZonedDateTime createdAt
) { }
