package de.innovationhub.prox.modules.project.application.project.dto;

import de.innovationhub.prox.modules.project.application.discipline.dto.ReadDisciplineDto;
import de.innovationhub.prox.modules.project.application.module.dto.ReadModuleTypeDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ReadProjectDto(
    UUID id,
    String title,
    String summary,
    String description,
    String requirement,
    AuthorDto author,
    PartnerDto partner,
    CurriculumContextDto curriculumContext,
    ProjectStatusDto status,
    TimeBoxDto timeBox,
    List<SupervisorDto> supervisors,
    List<String> tags
) {
  public record AuthorDto(
      UUID userId
  ) {}

  public record CurriculumContextDto(
      List<ReadDisciplineDto> disciplines,
      List<ReadModuleTypeDto> moduleTypes
  ) {}

  public record PartnerDto(
      UUID id,
      String name
  ) {}

  public record ProjectStatusDto(
      ProjectState state,
      Instant updatedAt
  ) {}

  public record SupervisorDto(
      UUID id,
      String name
  ) {}
  public record TimeBoxDto(
      LocalDate start,
      LocalDate end
  ) {}

}
