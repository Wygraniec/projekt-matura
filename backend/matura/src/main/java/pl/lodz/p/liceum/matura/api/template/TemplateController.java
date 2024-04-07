package pl.lodz.p.liceum.matura.api.template;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.liceum.matura.appservices.TemplateApplicationService;
import pl.lodz.p.liceum.matura.domain.template.TaskLanguage;
import pl.lodz.p.liceum.matura.domain.template.Template;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/templates")
public class TemplateController {
    private final TemplateApplicationService templateService;
    private final TemplateDtoMapper templateMapper;
    private final PageTemplateDtoMapper pageTemplateDtoMapper;

    @GetMapping(path = "/{id}")
    public ResponseEntity<TemplateDto> getTemplate(@PathVariable Integer id) {
        return ResponseEntity.ok(
                templateMapper.toDto(templateService.findById(id))
        );
    }

    @GetMapping
    public ResponseEntity<PageTemplateDto> getTemplates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageTemplateDto pageTemplate = pageTemplateDtoMapper.toPageDto(templateService.findAll(pageable));

        return ResponseEntity.ok(pageTemplate);
    }
    @GetMapping(params = "taskLanguage")
    public ResponseEntity<PageTemplateDto> getTemplatesByTaskLanguage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam TaskLanguage taskLanguage
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageTemplateDto pageTemplate = pageTemplateDtoMapper.toPageDto(templateService.findByTaskLanguage(taskLanguage, pageable));

        return ResponseEntity.ok(pageTemplate);
    }

    @PostMapping
    public ResponseEntity<TemplateDto> saveTemplate(@RequestBody TemplateDto dto) {
        Template template = templateService.save(templateMapper.toDomain(dto));

        return ResponseEntity.ok(
                templateMapper.toDto(template)
        );
    }

    @PutMapping
    public ResponseEntity<Void> updateTemplate(@RequestBody TemplateDto dto) {
        templateService.update(templateMapper.toDomain(dto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeTemplate(@PathVariable Integer id) {
        templateService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
