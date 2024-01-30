package pl.lodz.p.liceum.matura.domain.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.ZonedDateTime;

@ToString
@Data
@AllArgsConstructor
public class Template {
    Integer id;
    String sourceUrl;
    TaskLanguage taskLanguage;
    String source;
    Integer createdBy;
    ZonedDateTime createdAt;

    public boolean isUserTheOwnerOfThisTemplate(Integer userId) {
        return createdBy.equals(userId);
    }
}
