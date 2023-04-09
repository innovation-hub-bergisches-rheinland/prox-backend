package de.innovationhub.prox.modules.project.application.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.modules.project.application.discipline.dto.ReadDisciplineDto;
import de.innovationhub.prox.modules.project.application.module.dto.ReadModuleTypeDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(name = "Project")
public record ProjectDto(
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
    List<ProjectTagDto> tags,
    @JsonProperty("_permissions")
    ProjectPermissions permissions,
    ProjectMetrics metrics,
    Instant createdAt,
    Instant modifiedAt
) {
  @Schema(name = "Author")
  public record AuthorDto(
      UUID userId,
      String name
  ) {}

  @Schema(name = "CurriculumContext")
  public record ReadCurriculumContextDto(
      List<ReadDisciplineDto> disciplines,
      List<ReadModuleTypeDto> moduleTypes
  ) {

  }

  @Schema(name = "Partner")
  public record ReadPartnerDto(
      UUID id,
      String name
  ) {

  }

  @Schema(name = "ProjectStatus")
  public record ReadProjectStatusDto(
      ProjectState state,
      boolean acceptsInterest,
      boolean acceptsCommitment,
      Instant updatedAt
  ) {

  }

  @Schema(name = "Supervisor")
  public record ReadSupervisorDto(
      UUID id,
      String name
  ) {

  }

  @Schema(name = "TimeBox")
  public record ReadTimeBoxDto(
      LocalDate start,
      LocalDate end
  ) {

  }

}
