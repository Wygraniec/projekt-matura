package pl.lodz.p.liceum.matura.domain.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.liceum.matura.BaseIT;
import pl.lodz.p.liceum.matura.TestTemplateFactory;

class TemplateServiceIT extends BaseIT {

    @Autowired
    TemplateService service;

    @Test
    void add_user_test() {
        //given
        Template template = TestTemplateFactory.createCSharpTemplate();
        service.save(template);

        //when
        Template readTemplate = service.findBySourceUrl(template.getSourceUrl());

        //then
        Assertions.assertEquals(template.getSourceUrl(), readTemplate.getSourceUrl());
        Assertions.assertEquals(template.getTaskLanguage(), readTemplate.getTaskLanguage());
        Assertions.assertEquals(template.getSource(), readTemplate.getSource());
    }

    @Test
    void get_by_email_should_return_correct_user() {
        //given
        Template CSharpTemplate = TestTemplateFactory.createCSharpTemplate();
        Template PythonTemplate = TestTemplateFactory.createPythonTemplate();
        Template JavaTemplate = TestTemplateFactory.createJavaTemplate();
        service.save(CSharpTemplate);
        service.save(PythonTemplate);
        service.save(JavaTemplate);

        //when
        Template readTemplate= service.findBySourceUrl(PythonTemplate.getSourceUrl());

        //then
        Assertions.assertEquals(PythonTemplate.getSourceUrl(), readTemplate.getSourceUrl());
        Assertions.assertEquals(PythonTemplate.getTaskLanguage(), readTemplate.getTaskLanguage());
        Assertions.assertEquals(PythonTemplate.getSource(), readTemplate.getSource());
    }
}