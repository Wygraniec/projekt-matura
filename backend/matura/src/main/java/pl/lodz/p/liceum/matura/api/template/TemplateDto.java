package pl.lodz.p.liceum.matura.api.template;

import java.time.ZonedDateTime;

public record TemplateDto(
        Integer id,
        String sourceUrl,
        String taskLanguage,
        String source,
        String statement,
        Integer numberOfSubtasks,
        Integer createdBy,
        ZonedDateTime createdAt
) { }
