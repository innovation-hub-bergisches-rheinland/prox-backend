package de.innovationhub.prox.modules.project.application.module.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class FindModulesByDisciplinesHandler {

  private final ModuleTypeRepository moduleTypeRepository;

  public List<ModuleType> handle(List<String> disciplineKeys) {
    if (disciplineKeys == null) {
      throw new IllegalArgumentException("disciplineKeys must not be null");
    }

    if (disciplineKeys.isEmpty()) {
      return StreamSupport.stream(moduleTypeRepository.findAll().spliterator(), false)
          .toList();
    }

    return moduleTypeRepository.findByDisciplineKeys(disciplineKeys);
  }
}
