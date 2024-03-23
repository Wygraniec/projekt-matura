package pl.lodz.p.liceum.matura.api.result;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.liceum.matura.domain.result.Result;
import pl.lodz.p.liceum.matura.domain.result.ResultService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/results")
public class ResultController {

    private final ResultService resultService;
    private final ResultDtoMapper mapper;

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResultDto> getResult(@PathVariable Integer id) {
        Result result = resultService.findById(id);
        return ResponseEntity.ok(mapper.toDto(result));
    }
}
