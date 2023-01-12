package de.innovationhub.prox.modules.project.application.project.web.dto;

import de.innovationhub.prox.modules.profile.contract.LecturerView;
import de.innovationhub.prox.modules.profile.contract.OrganizationView;
import de.innovationhub.prox.modules.project.application.discipline.web.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.application.module.web.dto.ModuleTypeMapper;
import de.innovationhub.prox.modules.project.application.project.web.dto.ProjectDto.ReadProjectStatusDto;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectStatus;
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
  @Mapping(target = "supervisors", source = "lecturerView")
  ProjectDto toDto(Project project, List<LecturerView> lecturerView,
      OrganizationView organizationView, List<String> tags, ProjectPermissions permissions, ProjectMetrics metrics);

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
