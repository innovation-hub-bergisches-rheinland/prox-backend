package de.innovationhub.prox.modules.project.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectFacade {
  Optional<ProjectView> get(UUID id);
  List<ProjectView> findAllWithAnyTags(List<UUID> tags);
}
