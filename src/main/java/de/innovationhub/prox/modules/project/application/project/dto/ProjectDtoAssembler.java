package de.innovationhub.prox.modules.project.application.project.dto;

import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.organization.contract.dto.OrganizationDto;
import de.innovationhub.prox.modules.project.application.ProjectPermissionEvaluator;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectPermissions;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProjectDtoAssembler {

  private final TagCollectionFacade tagCollectionFacade;
  private final OrganizationFacade organizationFacade;
  private final UserProfileFacade userProfileFacade;
  private final ModuleTypeRepository moduleTypeRepository;
  private final DisciplineRepository disciplineRepository;
  private final ProjectMapper projectMapper;

  // TODO: EXPERIMENTAL
  private final ProjectPermissionEvaluator projectPermissionEvaluator;
  private final AuthenticationFacade authenticationFacade;

  public ProjectDto toDto(Project project) {
    OrganizationDto partnerOrg = null;

    if (project.getPartner() != null) {
      var orgId = project.getPartner().getOrganizationId();
      partnerOrg = organizationFacade.get(orgId)
          .orElse(new OrganizationDto(orgId));
    }

    List<TagDto> tags = tagCollectionFacade.getTagCollection(project.getTagCollectionId())
        .map(TagCollectionDto::tags)
        .orElse(Collections.emptyList());

    var supervisors = userProfileFacade.findByUserId(
        project.getSupervisors()
            .stream()
            .map(Supervisor::getLecturerId)
            .toList()
    );

    var author = userProfileFacade.getByUserId(project.getAuthor().getUserId()).orElse(null);

    var permissions = evaluatePermissions(project); // TODO

    List<Discipline> disciplines = List.of();
    List<ModuleType> moduleTypes = List.of();
    if (project.getCurriculumContext() != null) {
      disciplines = disciplineRepository.findByKeyIn(
          project.getCurriculumContext().getDisciplines());
      moduleTypes = moduleTypeRepository.findByKeyIn(
          project.getCurriculumContext().getModuleTypes());
    }

    return projectMapper.toDto(project, disciplines, moduleTypes, supervisors, partnerOrg, tags,
        permissions, author);
  }

  public Page<ProjectDto> toDto(Page<Project> projects) {
    return projects.map(this::toDto);
  }

  public ProjectPermissions evaluatePermissions(Project project) {
    var authentication = authenticationFacade.getAuthentication();
    return new ProjectPermissions(
        projectPermissionEvaluator.hasPermission(project, authentication)
    );
  }
}