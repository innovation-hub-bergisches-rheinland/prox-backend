package de.innovationhub.prox.modules.project.contract;

import java.util.List;
import java.util.UUID;

public interface ProjectFacade {
  List<ProjectView> findAllWithAnyTags(List<UUID> tags);
}
