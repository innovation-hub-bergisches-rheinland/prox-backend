package de.innovationhub.prox.modules.project.application.project.dto;

import de.innovationhub.prox.modules.organization.contract.dto.OrganizationDto;
import de.innovationhub.prox.modules.project.application.discipline.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.application.module.dto.ModuleTypeMapper;
import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.contract.dto.ModuleTypeDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto.AuthorDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto.ReadCurriculumContextDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto.ReadProjectStatusDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto.ReadSupervisorDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectPermissions;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectStatus;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
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
  @Mapping(target = "curriculumContext", expression = "java( toDto(disciplines, moduleTypes) )")
  @Mapping(target = "createdAt", source = "project.createdAt")
  @Mapping(target = "modifiedAt", source = "project.modifiedAt")
  @Mapping(target = "permissions", source = "permissions")
  ProjectDto toDto(Project project, List<Discipline> disciplines, List<ModuleType> moduleTypes,
      List<UserProfileDto> userProfileView,
      OrganizationDto organizationView, List<TagDto> tags, ProjectPermissions permissions,
      UserProfileDto author);

  default AuthorDto toSupervisors(UserProfileDto author) {
    if (author == null) {
      return null;
    }
    return new AuthorDto(author.userId(), author.displayName());
  }

  default List<ReadSupervisorDto> toSupervisors(List<UserProfileDto> userProfileView) {
    if (userProfileView == null) {
      return List.of();
    }
    return userProfileView.stream()
        .map(l -> new ReadSupervisorDto(l.userId(), l.displayName()))
        .toList();
  }

  default ReadCurriculumContextDto toDto(List<Discipline> disciplines,
      List<ModuleType> moduleTypes) {
    return new ReadCurriculumContextDto(toDisciplineDto(disciplines), toModuleTypeDto(moduleTypes));
  }

  default List<DisciplineDto> toDisciplineDto(List<Discipline> disciplines) {
    if (disciplines == null) {
      return List.of();
    }
    return disciplines.stream()
        .map(d -> new DisciplineDto(d.getKey(), d.getName(), d.getCreatedAt(),
            d.getModifiedAt()))
        .toList();
  }

  default List<ModuleTypeDto> toModuleTypeDto(List<ModuleType> moduleTypes) {
    if (moduleTypes == null) {
      return List.of();
    }
    return moduleTypes.stream()
        .map(d -> new ModuleTypeDto(d.getKey(), d.getName(), d.getCreatedAt(),
            d.getModifiedAt()))
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
