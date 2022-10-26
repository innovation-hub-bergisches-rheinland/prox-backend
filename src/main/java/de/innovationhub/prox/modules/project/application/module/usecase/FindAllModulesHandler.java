package de.innovationhub.prox.modules.project.application.module.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class FindAllModulesHandler implements UseCaseHandler<List<ModuleType>, FindAllModules> {
  private final ModuleTypeRepository moduleTypeRepository;

  @Override
  public List<ModuleType> handle(FindAllModules useCase) {
    return StreamSupport.stream(moduleTypeRepository.findAll().spliterator(), false)
        .toList();
  }
}
