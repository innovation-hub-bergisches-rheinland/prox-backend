package de.innovationhub.prox.modules.project.application.discipline.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class FindAllDisciplinesHandler implements UseCaseHandler<List<Discipline>, FindAllDisciplines> {
  private final DisciplineRepository disciplineRepository;

  @Override
  public List<Discipline> handle(FindAllDisciplines useCase) {
    return StreamSupport.stream(disciplineRepository.findAll().spliterator(), false)
        .toList();
  }
}
