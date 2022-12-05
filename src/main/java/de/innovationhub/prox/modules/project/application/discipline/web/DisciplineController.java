package de.innovationhub.prox.modules.project.application.discipline.web;

import de.innovationhub.prox.modules.project.application.discipline.usecase.FindAllDisciplinesHandler;
import de.innovationhub.prox.modules.project.application.discipline.web.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.application.discipline.web.dto.ReadDisciplineDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("disciplines")
@Tag(name = "Curriculum")
public class DisciplineController {

  private final FindAllDisciplinesHandler findAllDisciplines;
  private final DisciplineMapper disciplineMapper;

  public DisciplineController(FindAllDisciplinesHandler findAllDisciplines,
      DisciplineMapper disciplineMapper) {
    this.findAllDisciplines = findAllDisciplines;
    this.disciplineMapper = disciplineMapper;
  }

  @GetMapping
  public ResponseEntity<Page<ReadDisciplineDto>> getAll(@ParameterObject @PageableDefault(size = 100) Pageable pageable) {
    return ResponseEntity.ok(
        disciplineMapper.toDtoPage(
            findAllDisciplines.handle(pageable)
        )
    );
  }
}
