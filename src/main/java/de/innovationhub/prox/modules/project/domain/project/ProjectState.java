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
      OFFERED, List.of(RUNNING),
      RUNNING, List.of(COMPLETED),
      COMPLETED, List.of()
  );

}
