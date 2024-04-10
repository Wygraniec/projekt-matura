import axios from "axios";
import {User} from "./userService.js";

const API = `${import.meta.env.VITE_API_URL}/v1`;

export class Template {
    constructor(id, language, statement, source, createdAt) {
        this.id = id
        this.language = language
        this.statement = statement
        this.source = source
        this.createdAt = createdAt
    }
}

export class TemplatePage {
    constructor(templates, currentPage, totalPages, totalElements) {
        this.templates = templates
        this.currentPage = currentPage
        this.totalPages = totalPages
        this.totalElements = totalElements
    }
}

const markdown = `
Zadanie 4. Liczby binarne
=================

W pliku \`liczby.txt\` znajduje się n (1 <= n <= 400000) liczb naturalnych zapisanych binarnie. Każda
liczba zapisana jest w osobnym wierszu. Pierwsze pięć wierszy zawiera następujące liczby:
- 11010100111
- 11110111111011101
- 1010100111010100
- 1101111111111111111111010100101010101001
- 1010110011001101010011110101010101010111


Każda liczba binarna zawiera co najwyżej **250 cyfr binarnych**, co oznacza, że w wielu
językach programowania wartości niektórych z tych liczb nie da się zapamiętać
w pojedynczej zmiennej typu całkowitoliczbowego, np. w języku C++ w zmiennej typu
\`int\`.\
Napisz **program**, który da odpowiedzi do poniższych zadań. Odpowiedzi zapisz w pliku
\`wynik4.txt\`, a każdą odpowiedź poprzedź numerem oznaczającym odpowiednie zadanie.


## Zadanie 1. (0-3)


Podaj, ile liczb z pliku \`liczby.txt\` ma w swoim zapisie binarnym więcej zer niż jedynek.


*Przykład*: Dla zestawu liczb:
- 101011010011001100111
- <u>10001001</u>
- <u>1000000</u>
- 101010011100
- <u>100010</u>


wynikiem jest liczba 3 (3 podkreślone liczby mają w swoim zapisie więcej zer niż jedynek).


## Zadanie 2. (0-3)

Podaj, ile liczb w pliku \`liczby.txt\` jest podzielnych przez 2 oraz ile liczb jest podzielnych
przez 8.


*Przykład*: Dla zestawu liczb:
- 101011010011001100000 (*), (**)
- 10001001
- 100100 (*)
- 101010010101011011000 (*), (**)
- 100011

trzy liczby są podzielne przez 2 (*) i dwie liczby są podzielne przez 8 (**).

## Zadanie 3 (0-6)

Znajdź najmniejszą i największą liczbę w pliku \`liczby.txt\`. Jako odpowiedź podaj
numery wierszy, w których się one znajdują.

*Przykład*: Dla zestawu liczb:
- 101011010011001100111
- 10001001011101010
- 1001000
- 101010011100
- 1000110

najmniejsza liczba to: \`1000110\` 

największa liczba to: \`101011010011001100111\` 

Prawidłowa odpowiedź dla powyższego przykładu to: \`5\`, \`1\`. 
`;

export const getTemplates = async (page = 0, pageSize = 10) => {
    let request = await axios.get(`${API}/templates?page=${page}&size=${pageSize}`, User.fromLocalStorage().getAuthHeader())

    // TODO add reading the statement from the DB, as soon as it's implemented on the backend
    return new TemplatePage(
        request.data['templates'].map(data => new Template(data['id'], data['taskLanguage'], markdown, data['source'], new Date(data['createdAt']))),
        request.data['currentPage'],
        request.data['totalPages'],
        request.data['totalElements']
    )
}


