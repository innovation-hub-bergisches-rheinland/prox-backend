package de.innovationhub.prox.modules.project.application.project.dto;

import de.innovationhub.prox.modules.organization.contract.OrganizationView;
import de.innovationhub.prox.modules.project.application.discipline.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.application.module.dto.ModuleTypeMapper;
import de.innovationhub.prox.modules.project.application.project.dto.ProjectDto.AuthorDto;
import de.innovationhub.prox.modules.project.application.project.dto.ProjectDto.ReadProjectStatusDto;
import de.innovationhub.prox.modules.project.application.project.dto.ProjectDto.ReadSupervisorDto;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectStatus;
import de.innovationhub.prox.modules.user.contract.lecturer.UserProfileView;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
    ModuleTypeMapper.class,
    DisciplineMapper.class
})
interface ProjectMapper {

  @Mapping(target = ".", source = "project")
  @Mapping(target = "id", source = "project.id")
  @Mapping(target = "timeBox.start", source = "project.timeBox.startDate")
  @Mapping(target = "timeBox.end", source = "project.timeBox.endDate")
  @Mapping(target = "tags", source = "tags")
  @Mapping(target = "partner", source = "organizationView")
  @Mapping(target = "supervisors", source = "userProfileView")
  @Mapping(target = "author", source = "author")
  ProjectDto toDto(Project project, List<UserProfileView> userProfileView,
      OrganizationView organizationView, List<String> tags, ProjectPermissions permissions,
      UserProfileView author,
      ProjectMetrics metrics);

  default AuthorDto toSupervisors(UserProfileView author) {
    if (author == null) {
      return null;
    }
    return new AuthorDto(author.id(), author.displayName());
  }

  default List<ReadSupervisorDto> toSupervisors(List<UserProfileView> userProfileView) {
    if (userProfileView == null) {
      return List.of();
    }
    return userProfileView.stream()
        .map(l -> new ReadSupervisorDto(l.id(), l.displayName()))
        .toList();
  }

  default ReadProjectStatusDto toDto(ProjectStatus status) {
    if (status == null) {
      return null;
    }

    return new ReadProjectStatusDto(
        status.getState(),
        status.acceptsInterest(),
        status.acceptsCommitment(),
        status.getUpdatedAt()
    );
  }
}
