package de.innovationhub.prox.modules.project.application.module.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class FindAllModulesHandler {

  private final ModuleTypeRepository moduleTypeRepository;

  public List<ModuleType> handle() {
    return StreamSupport.stream(moduleTypeRepository.findAll().spliterator(), false)
        .toList();
  }
}
