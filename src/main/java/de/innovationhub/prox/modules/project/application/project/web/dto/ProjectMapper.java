package de.innovationhub.prox.modules.project.application.project.web.dto;

import de.innovationhub.prox.modules.organization.contract.OrganizationView;
import de.innovationhub.prox.modules.project.application.discipline.web.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.application.module.web.dto.ModuleTypeMapper;
import de.innovationhub.prox.modules.project.application.project.web.dto.ProjectDto.ReadProjectStatusDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.ProjectDto.ReadSupervisorDto;
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
  ProjectDto toDto(Project project, List<UserProfileView> userProfileView,
      OrganizationView organizationView, List<String> tags, ProjectPermissions permissions,
      ProjectMetrics metrics);

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
