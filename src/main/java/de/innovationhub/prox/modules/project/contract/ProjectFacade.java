package de.innovationhub.prox.modules.project.contract;

import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectFacade {
  Optional<ProjectDto> get(UUID id);
  List<ProjectDto> findAllWithAnyTags(List<UUID> tags);
}
