package de.innovationhub.prox.modules.project.application.project.web.dto;


import java.time.LocalDate;

public record CreateProjectRequest(
    String title,
    String description,
    String summary,
    String requirement,
    CurriculumContextRequest context,
    TimeBoxDto timeBox) {
  public record TimeBoxDto(
      LocalDate start,
      LocalDate end
  ) {

  }
}
