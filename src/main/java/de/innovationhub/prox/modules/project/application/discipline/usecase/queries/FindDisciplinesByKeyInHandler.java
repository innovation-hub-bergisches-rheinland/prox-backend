package de.innovationhub.prox.modules.project.application.discipline.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class FindDisciplinesByKeyInHandler {

  private final DisciplineRepository disciplineRepository;

  public List<Discipline> handle(Collection<String> keys) {
    return StreamSupport.stream(disciplineRepository.findByKeyIn(new ArrayList<>(keys)).spliterator(), false)
        .toList();
  }
}
