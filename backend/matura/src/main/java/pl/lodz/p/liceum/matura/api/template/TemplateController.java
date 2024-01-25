package pl.lodz.p.liceum.matura.api.template;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.liceum.matura.appservices.TemplateApplicationService;
import pl.lodz.p.liceum.matura.domain.template.Template;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/templates")
public class TemplateController {
    private final TemplateApplicationService templateService;
    private final TemplateDtoMapper templateMapper;

    @GetMapping(path = "/{id}")
    public ResponseEntity<TemplateDto> getTemplate(@PathVariable Integer id) {
        return ResponseEntity.ok(
                templateMapper.toDto(templateService.findById(id))
        );
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
