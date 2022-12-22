package de.innovationhub.prox.modules.project.application.project.web.dto;


import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

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
