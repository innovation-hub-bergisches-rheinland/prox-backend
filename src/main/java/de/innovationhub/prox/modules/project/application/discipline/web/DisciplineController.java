package de.innovationhub.prox.modules.project.application.discipline.web;

import de.innovationhub.prox.modules.project.application.discipline.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.application.discipline.dto.ReadDisciplineDto;
import de.innovationhub.prox.modules.project.application.discipline.usecase.FindAllDisciplines;
import de.innovationhub.prox.modules.project.application.discipline.usecase.FindAllDisciplinesHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("disciplines")
public class DisciplineController {

  private final FindAllDisciplinesHandler findAllDisciplines;
  private final DisciplineMapper disciplineMapper;

  public DisciplineController(FindAllDisciplinesHandler findAllDisciplines,
      DisciplineMapper disciplineMapper) {
    this.findAllDisciplines = findAllDisciplines;
    this.disciplineMapper = disciplineMapper;
  }

  @GetMapping
  public ResponseEntity<List<ReadDisciplineDto>> getAll() {
    return ResponseEntity.ok(
        disciplineMapper.toDtoList(
            findAllDisciplines.handle(new FindAllDisciplines())
        )
    );
  }
}
