package de.innovationhub.prox.modules.project.application.project.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.modules.project.application.discipline.web.dto.ReadDisciplineDto;
import de.innovationhub.prox.modules.project.application.module.web.dto.ReadModuleTypeDto;
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
    ReadPartnerDto partner,
    ReadCurriculumContextDto curriculumContext,
    ReadProjectStatusDto status,
    ReadTimeBoxDto timeBox,
    List<ReadSupervisorDto> supervisors,
    List<String> tags,
    @JsonProperty("_permissions")
    ProjectPermissions permissions,
    Instant createdAt,
    Instant modifiedAt
) {
  public record AuthorDto(
      UUID userId
  ) {}

  public record ReadCurriculumContextDto(
      List<ReadDisciplineDto> disciplines,
      List<ReadModuleTypeDto> moduleTypes
  ) {

  }

  public record ReadPartnerDto(
      UUID id,
      String name
  ) {

  }

  public record ReadProjectStatusDto(
      ProjectState state,
      Instant updatedAt
  ) {

  }

  public record ReadSupervisorDto(
      UUID id,
      String name
  ) {

  }

  public record ReadTimeBoxDto(
      LocalDate start,
      LocalDate end
  ) {

  }

}
