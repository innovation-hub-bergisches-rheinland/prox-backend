package de.innovationhub.prox.modules.project;

import de.innovationhub.prox.modules.project.domain.project.Author;
import de.innovationhub.prox.modules.project.domain.project.Partner;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.project.domain.project.ProjectStatus;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ProjectFixtures {
  public static final UUID A_PROJECT_CREATOR_ID = UUID.randomUUID();
  public static final UUID A_PROJECT_PARTNER_ID = UUID.randomUUID();
  public static final Project A_PROJECT = new Project(
        UUID.randomUUID(),
        new Author(A_PROJECT_CREATOR_ID),
        new Partner(A_PROJECT_PARTNER_ID),
        "Test",
        "Test",
        "Test",
        "Test",
        null,
        new ProjectStatus(ProjectState.PROPOSED, Instant.now()),
        null,
        Collections.emptyList(),
        null);

  public static List<Project> ALL = List.of(A_PROJECT);
}
