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
public class FindModulesByDisciplinesHandler implements UseCaseHandler<List<ModuleType>, FindModulesByDisciplines> {
  private final ModuleTypeRepository moduleTypeRepository;

  @Override
  public List<ModuleType> handle(FindModulesByDisciplines useCase) {
    var keys = useCase.disciplineKeys();
    if(keys.isEmpty()) {
      return StreamSupport.stream(moduleTypeRepository.findAll().spliterator(), false)
          .toList();
    }

    return moduleTypeRepository.findByDisciplineKeys(keys);
  }
}
