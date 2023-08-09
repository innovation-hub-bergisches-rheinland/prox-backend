package de.innovationhub.prox.modules.project.contract;

import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.contract.dto.ModuleTypeDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectFacade {

  Optional<ProjectDto> get(UUID id);

  List<ProjectDto> findAllByIds(Collection<UUID> ids);

  List<DisciplineDto> findDisciplinesByKeyIn(Collection<String> keys);

  List<ModuleTypeDto> findModuleTypesByKeyIn(Collection<String> keys);

  int countProjects(Collection<ProjectState> status,
      Collection<String> specializationKeys,
      Collection<String> moduleTypeKeys,
      String text,
      Collection<UUID> tagIds,
      Instant modifiedSince);
}
