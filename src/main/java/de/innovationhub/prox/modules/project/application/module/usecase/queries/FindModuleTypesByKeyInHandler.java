package de.innovationhub.prox.modules.project.application.module.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class FindModuleTypesByKeyInHandler {

  private final ModuleTypeRepository moduleTypeRepository;

  public List<ModuleType> handle(Collection<String> keys) {
    return StreamSupport.stream(moduleTypeRepository.findByKeyIn(new ArrayList<>(keys)).spliterator(), false)
        .toList();
  }
}
