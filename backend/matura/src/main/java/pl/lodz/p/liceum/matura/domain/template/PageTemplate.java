package pl.lodz.p.liceum.matura.domain.template;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class PageTemplate implements Serializable {

    List<Template> templates;
    Integer currentPage;
    Integer totalPages;
    Long totalElements;
}