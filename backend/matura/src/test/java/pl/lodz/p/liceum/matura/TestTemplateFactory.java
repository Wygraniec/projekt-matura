package pl.lodz.p.liceum.matura;

import pl.lodz.p.liceum.matura.domain.template.TaskLanguage;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserRole;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestTemplateFactory {

    private static int templateSequence = 10;

    public static Template createCSharpTemplate() {
        templateSequence++;
        return new Template(
                templateSequence,
                "https://github.com/Wygraniec/projekt-matura",
                TaskLanguage.C_SHARP,
                "Matura 05.2023",
                "C# task statement",
                0,
                ZonedDateTime.of(2023, 6, 17, 12, 40, 00, 0, ZoneId.of("UTC"))
        );
    }

    public static Template createPythonTemplate() {
        templateSequence++;
        return new Template(
                templateSequence,
                "https://github.com/amigoscode/cohort-2-e",
                TaskLanguage.PYTHON,
                "Matura 05.2023",
                "Python task statement",
                0,
                ZonedDateTime.of(2023, 6, 17, 12, 40, 00, 0, ZoneId.of("UTC"))
        );
    }

    public static Template createJavaTemplate() {
        templateSequence++;
        return new Template(
                templateSequence,
                "https://github.com/amigoscode/spring-boot-fullstack-professional/",
                TaskLanguage.JAVA,
                "Matura 05.2023",
                "Java task statement",
                0,
                ZonedDateTime.of(2023, 6, 17, 12, 40, 00, 0, ZoneId.of("UTC"))
        );
    }
}
