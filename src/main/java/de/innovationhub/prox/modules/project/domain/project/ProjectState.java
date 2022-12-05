package de.innovationhub.prox.modules.project.domain.project;

import java.util.List;
import java.util.Map;

public enum ProjectState {
  PROPOSED,
  ARCHIVED,
  STALE,
  OFFERED,
  RUNNING,
  COMPLETED;

  public static final Map<ProjectState, List<ProjectState>> TRANSITIONS = Map.of(
      PROPOSED, List.of(ARCHIVED, OFFERED),
      ARCHIVED, List.of(PROPOSED, STALE),
      STALE, List.of(),

      // At the moment we permit every transition combination of Offered, Running and Completed.
      // This is because we don't have a clear definition of the states yet and want to allow the
      // lecturers to change the status by themselves.
      // Later, we want to perform status transitions automatically based on the project's progress.
      OFFERED, List.of(RUNNING, COMPLETED),
      RUNNING, List.of(OFFERED, COMPLETED),
      COMPLETED, List.of(OFFERED, RUNNING)
  );

}
