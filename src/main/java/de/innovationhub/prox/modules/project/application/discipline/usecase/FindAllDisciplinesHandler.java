package de.innovationhub.prox.modules.project.application.discipline.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@ApplicationComponent
public class FindAllDisciplinesHandler {

  private final DisciplineRepository disciplineRepository;

  public Page<Discipline> handle(Pageable pageable) {
    return disciplineRepository.findAll(pageable);
  }
}
