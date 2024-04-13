package pl.lodz.p.liceum.matura.domain.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.liceum.matura.domain.user.*;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @InjectMocks
    private TemplateService templateService;

    private final Template fakeTemplate = new Template(
            1,
            "newPythonTemplate.example.com",
            TaskLanguage.PYTHON,
            "Matura 05.2023",
            "Task statement",
            0,
            ZonedDateTime.of(2023, 6, 17, 12, 30, 20, 0, ZoneId.of("UTC"))
    );

    private static ZonedDateTime NOW = ZonedDateTime.of(
            2023,
            6,
            17,
            12,
            30,
            20,
            0,
            ZoneId.of("UTC")
    );

    @Test
    void update_method_should_not_throw_exception() {
        // Expect
        Assertions.assertDoesNotThrow(() -> templateService.update(fakeTemplate));
    }

    @Test
    void delete_method_should_not_throw_exception() {
        // Expect
        Assertions.assertDoesNotThrow(() -> templateService.removeById(fakeTemplate.getId()));
    }

    @Test
    void save_method_should_return_saved_template_when_template_does_not_exist() {
        Mockito.when(templateRepository.save(Mockito.any(Template.class))).thenReturn(fakeTemplate);

        //when
        Template savedTemplate = templateService.save(fakeTemplate);

        //then
        Assertions.assertNotNull(savedTemplate);
        Assertions.assertEquals(fakeTemplate.getId(), savedTemplate.getId());
        Assertions.assertEquals(fakeTemplate.getSourceUrl(), savedTemplate.getSourceUrl());
        Assertions.assertEquals(fakeTemplate.getTaskLanguage(), savedTemplate.getTaskLanguage());
        Assertions.assertEquals(fakeTemplate.getSource(), savedTemplate.getSource());
    }

    @Test
    void save_method_should_throw_template_already_exists_exception_when_template_exists() {
        Mockito.when(templateRepository.save(Mockito.any(Template.class)))
                .thenThrow(new TemplateAlreadyExistsException());

        //when
        //then
        Assertions.assertThrows(TemplateAlreadyExistsException.class,
                ()-> templateService.save(fakeTemplate));
    }

    @Test
    void find_by_source_url_method_should_return_founded_template_when_template_exists() {
        Mockito.when(templateRepository.findBySourceUrl(fakeTemplate.getSourceUrl())).thenReturn(Optional.of(fakeTemplate));

        //when
        Template foundedTemplate = templateService.findBySourceUrl(fakeTemplate.getSourceUrl());

        //then
        Assertions.assertNotNull(foundedTemplate);
        Assertions.assertEquals(fakeTemplate.getId(), foundedTemplate.getId());
        Assertions.assertEquals(fakeTemplate.getSourceUrl(), foundedTemplate.getSourceUrl());
        Assertions.assertEquals(fakeTemplate.getTaskLanguage(), foundedTemplate.getTaskLanguage());
        Assertions.assertEquals(fakeTemplate.getSource(), foundedTemplate.getSource());
    }

    @Test
    void find_by_source_url_method_should_throw_template_not_found_exception_when_template_does_not_exist() {
        Mockito.when(templateRepository.findBySourceUrl(fakeTemplate.getSourceUrl())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThrows(TemplateNotFoundException.class,
                ()-> templateService.findBySourceUrl(fakeTemplate.getSourceUrl()));
    }

    @Test
    void find_by_id_method_should_return_founded_template_when_template_exists() {
        Mockito.when(templateRepository.findById(fakeTemplate.getId())).thenReturn(Optional.of(fakeTemplate));

        //when
        Template foundedTemplate = templateService.findById(fakeTemplate.getId());

        //then
        Assertions.assertNotNull(foundedTemplate);
        Assertions.assertEquals(fakeTemplate.getId(), foundedTemplate.getId());
        Assertions.assertEquals(fakeTemplate.getSourceUrl(), foundedTemplate.getSourceUrl());
        Assertions.assertEquals(fakeTemplate.getTaskLanguage(), foundedTemplate.getTaskLanguage());
        Assertions.assertEquals(fakeTemplate.getSource(), foundedTemplate.getSource());
    }

    @Test
    void find_by_id_method_should_throw_template_not_found_exception_when_template_does_not_exist() {
        Mockito.when(templateRepository.findById(fakeTemplate.getId())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThrows(TemplateNotFoundException.class,
                ()-> templateService.findById(fakeTemplate.getId()));
    }
}