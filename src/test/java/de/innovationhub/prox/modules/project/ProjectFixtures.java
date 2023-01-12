package de.innovationhub.prox.modules.project;

import de.innovationhub.prox.modules.project.domain.project.Author;
import de.innovationhub.prox.modules.project.domain.project.CurriculumContext;
import de.innovationhub.prox.modules.project.domain.project.Partner;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.project.domain.project.ProjectStatus;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ProjectFixtures {

  public static final UUID A_PROJECT_CREATOR_ID = UUID.fromString(
      "00000000-0000-0000-0000-000000000001");
  public static final UUID A_PROJECT_PARTNER_ID = UUID.randomUUID();

  public static List<Project> build_project_list() {
    return List.of(build_a_project());
  }

  public static Project build_a_project() {
    return new Project(
        UUID.randomUUID(),
        new Author(A_PROJECT_CREATOR_ID),
        new Partner(A_PROJECT_PARTNER_ID),
        "Test",
        "Test",
        "Test",
        "Test",
        new CurriculumContext(
            List.of(DisciplineFixtures.INF),
            List.of(ModuleTypeFixtures.BACHELOR_THESIS)
        ),
        new ProjectStatus(ProjectState.PROPOSED, Instant.now()),
        null,
        Collections.emptyList(),
        null,
        new HashSet<>());
  }
}
