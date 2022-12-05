package de.innovationhub.prox.modules.project.application.module.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@ApplicationComponent
public class FindAllModulesHandler {

  private final ModuleTypeRepository moduleTypeRepository;

  public Page<ModuleType> handle(Pageable pageable) {
    return moduleTypeRepository.findAll(pageable);
  }
}
