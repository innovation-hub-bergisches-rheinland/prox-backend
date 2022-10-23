package de.innovationhub.prox.modules.project.domain.project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {

  Project save(Project project);

  void delete(Project project);

  Optional<Project> getById(UUID id);

  List<Project> getAll();
}
