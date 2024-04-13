package pl.lodz.p.liceum.matura;

import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.lodz.p.liceum.matura.domain.template.TaskLanguage;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateService;

import java.time.ZonedDateTime;

@Component
@Log
public class DefaultTemplates implements CommandLineRunner {

    private final TemplateService templateService;

    public DefaultTemplates(TemplateService templateService) {
        this.templateService = templateService;
    }

    private final Template matura05_2023Python = new Template(
            null,
            "https://github.com/HubertM6/MaturaBinaryNumbersPython",
            TaskLanguage.PYTHON,
            "Matura 05.2023",
            "Matura 05.2023 Python",
            0,
            ZonedDateTime.now()
    );
    private final Template matura05_2023CSharp = new Template(
            null,
            "https://github.com/HubertM6/MaturaBinaryNumbersCSharp",
            TaskLanguage.C_SHARP,
            "Matura 05.2023",
            "Matura 05.2023 C#",
            0,
            ZonedDateTime.now()
    );
    private final Template matura05_2023Java = new Template(
            null,
            "https://github.com/HubertM6/MaturaBinaryNumbersJava",
            TaskLanguage.JAVA,
            "Matura 05.2023",
            "Matura 05.2023 Java",
            0,
            ZonedDateTime.now()
    );
    private final Template matura05_2022Python = new Template(
            null,
            "https://github.com",
            TaskLanguage.PYTHON,
            "Matura 05.2022",
            "Matura 05.2022 Python",
            0,
            ZonedDateTime.now()
    );
    private final Template OI31Etap1BudowaLotniskaPython = new Template(
            null,
            "https://github.com",
            TaskLanguage.PYTHON,
            "31 OI Etap 1 Budowa lotniska",
            "Etap 1, 31 OI, Budowa lotniska",
            0,
            ZonedDateTime.now()
    );

    @Override
    public void run(String... args) {
        try {
            addTemplate(matura05_2023Python);
            addTemplate(matura05_2023CSharp);
            addTemplate(matura05_2023Java);
            addTemplate(matura05_2022Python);
            addTemplate(OI31Etap1BudowaLotniskaPython);
        } catch (Exception ex) {
            log.warning(ex.getMessage());
        }
    }

    private void addTemplate(Template template) {
        templateService.save(template);
    }
}
