package de.innovationhub.prox.modules.project.application.discipline.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class FindAllDisciplinesHandler {

  private final DisciplineRepository disciplineRepository;

  public List<Discipline> handle() {
    return StreamSupport.stream(disciplineRepository.findAll().spliterator(), false)
        .toList();
  }
}
