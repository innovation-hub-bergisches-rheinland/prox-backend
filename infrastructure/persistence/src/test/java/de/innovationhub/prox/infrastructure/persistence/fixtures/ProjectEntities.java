package de.innovationhub.prox.infrastructure.persistence.fixtures;

import de.innovationhub.prox.infrastructure.persistence.model.ProjectEntity;
import java.util.List;
import java.util.UUID;

public class ProjectEntities {
  public static ProjectEntity DRINKING_DUFF = new ProjectEntity();
  public static List<ProjectEntity> PROJECTS = List.of(DRINKING_DUFF);

  static {
    DRINKING_DUFF.setTitle("Drinking as much duff as we can");
    DRINKING_DUFF.setSummary("We will drink much duff");
    DRINKING_DUFF.setCreator(UUID.randomUUID());
  }
}
