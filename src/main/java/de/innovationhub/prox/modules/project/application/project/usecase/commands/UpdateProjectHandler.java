package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectRequest;
import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectRequest.TimeBoxDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.CurriculumContextRequest;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import de.innovationhub.prox.modules.project.domain.project.CurriculumContext;
import de.innovationhub.prox.modules.project.domain.project.Partner;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import de.innovationhub.prox.modules.project.domain.project.TimeBox;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateProjectHandler {
  private final ProjectRepository projectRepository;
  private final ModuleTypeRepository moduleTypeRepository;
  private final DisciplineRepository disciplineRepository;
  private final AuthenticationFacade authenticationFacade;

  @PreAuthorize("@projectPermissionEvaluator.hasPermission(#projectId, authentication)")
  public Project handle(UUID projectId, CreateProjectRequest projectDto) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);

    var context = buildContext(projectDto.context());
    var timeBox = buildTimeBox(projectDto.timeBox());

    project.setCurriculumContext(context);
    project.setTimeBox(timeBox);

    project.setTitle(projectDto.title());
    project.setDescription(projectDto.description());
    project.setSummary(projectDto.summary());
    project.setRequirement(projectDto.requirement());
    project.setPartner(projectDto.partnerId() != null ? new Partner(projectDto.partnerId()) : null);

    if(projectDto.supervisors() != null) {
      var supervisorList = projectDto.supervisors()
          .stream().map(Supervisor::new).toList();
      project.offer(supervisorList);
    }

    return this.projectRepository.save(project);
  }

  private TimeBox buildTimeBox(TimeBoxDto dto) {
    if (dto == null) {
      return null;
    }
    return new TimeBox(dto.start(), dto.end());
  }

  private CurriculumContext buildContext(CurriculumContextRequest dtoContext) {
    List<ModuleType> moduleTypes = List.of();
    List<Discipline> disciplines = List.of();
    if (dtoContext != null) {
      if (dtoContext.moduleTypeKeys() != null && !dtoContext.moduleTypeKeys().isEmpty()) {
        moduleTypes = moduleTypeRepository.findByKeyIn(dtoContext.moduleTypeKeys());
      }
      if (dtoContext.disciplineKeys() != null && !dtoContext.disciplineKeys().isEmpty()) {
        disciplines = disciplineRepository.findByKeyIn(dtoContext.disciplineKeys());
      }
    }

    return new CurriculumContext(disciplines, moduleTypes);
  }
}
