package de.innovationhub.prox.modules.project.application.project.web.dto;


import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public record CreateProjectRequest(
    String title,
    String description,
    String summary,
    String requirement,
    CurriculumContextRequest context,
    TimeBoxDto timeBox,
    @Nullable UUID partnerId,
    @Nullable Set<UUID> supervisors) {
  public record TimeBoxDto(
      LocalDate start,
      LocalDate end
  ) {

  }
}
