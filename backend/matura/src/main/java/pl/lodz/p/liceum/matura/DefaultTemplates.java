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
            """
                    Zadanie 4. Liczby binarne
                    =================
                                        
                    W pliku `liczby.txt` znajduje się n (1 <= n <= 400000) liczb naturalnych zapisanych binarnie. Każda
                    liczba zapisana jest w osobnym wierszu. Pierwsze pięć wierszy zawiera następujące liczby:
                    - 11010100111
                    - 11110111111011101
                    - 1010100111010100
                    - 1101111111111111111111010100101010101001
                    - 1010110011001101010011110101010101010111
                                        
                    Każda liczba binarna zawiera co najwyżej **250 cyfr binarnych**, co oznacza, że w wielu
                    językach programowania wartości niektórych z tych liczb nie da się zapamiętać
                    w pojedynczej zmiennej typu całkowitoliczbowego, np. w języku C++ w zmiennej typu
                    `int`.\\
                    Napisz **program**, który da odpowiedzi do poniższych zadań. Odpowiedzi zapisz w pliku
                    `wynik4.txt`, a każdą odpowiedź poprzedź numerem oznaczającym odpowiednie zadanie.\s
                                        
                    ## Zadanie 1. (0-3)
                                        
                    Podaj, ile liczb z pliku `liczby.txt` ma w swoim zapisie binarnym więcej zer niż jedynek.
                                        
                    *Przykład*: Dla zestawu liczb:
                    - 101011010011001100111
                    - <u>10001001</u>
                    - <u>1000000</u>
                    - 101010011100
                    - <u>100010</u>
                                        
                    wynikiem jest liczba 3 (3 podkreślone liczby mają w swoim zapisie więcej zer niż jedynek).
                                        
                    ## Zadanie 2. (0-3)
                                        
                    Podaj, ile liczb w pliku `liczby.txt` jest podzielnych przez 2 oraz ile liczb jest podzielnych
                    przez 8.
                                        
                    *Przykład*: Dla zestawu liczb:
                    - 101011010011001100000 (*), (**)
                    - 10001001
                    - 100100 (*)
                    - 101010010101011011000 (*), (**)
                    - 100011
                                        
                    trzy liczby są podzielne przez 2 (*) i dwie liczby są podzielne przez 8 (**).
                                        
                    ## Zadanie 3 (0-6)
                                        
                    Znajdź najmniejszą i największą liczbę w pliku `liczby.txt`. Jako odpowiedź podaj
                    numery wierszy, w których się one znajdują.
                                        
                    *Przykład*: Dla zestawu liczb:
                    - 101011010011001100111
                    - 10001001011101010
                    - 1001000
                    - 101010011100
                    - 1000110
                                        
                    najmniejsza liczba to: `1000110`\\
                    największa liczba to: `101011010011001100111`\\
                    Prawidłowa odpowiedź dla powyższego przykładu to: `5`, `1`.\s
                    """,
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
