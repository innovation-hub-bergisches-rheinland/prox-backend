package de.innovationhub.prox.modules.project.application.module.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@ApplicationComponent
public class FindModulesByDisciplinesHandler {

  private final ModuleTypeRepository moduleTypeRepository;

  public Page<ModuleType> handle(List<String> disciplineKeys, Pageable pageable) {
    if (disciplineKeys == null) {
      throw new IllegalArgumentException("disciplineKeys must not be null");
    }

    if (disciplineKeys.isEmpty()) {
      return moduleTypeRepository.findAll(pageable);
    }

    return moduleTypeRepository.findByDisciplineKeys(disciplineKeys, pageable);
  }
}
