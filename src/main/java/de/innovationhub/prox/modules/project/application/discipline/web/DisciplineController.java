package de.innovationhub.prox.modules.project.application.discipline.web;

import de.innovationhub.prox.modules.project.application.discipline.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.application.discipline.usecase.queries.FindAllDisciplinesHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
  public ResponseEntity<List<DisciplineDto>> getAll() {
    return ResponseEntity.ok(
        disciplineMapper.toDtoList(
            findAllDisciplines.handle()
        )
    );
  }
}
