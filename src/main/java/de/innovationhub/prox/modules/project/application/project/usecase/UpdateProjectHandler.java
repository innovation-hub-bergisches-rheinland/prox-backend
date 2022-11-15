package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.web.dto.CurriculumContextDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.PartnerDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SupervisorDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.TimeBoxDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.UpdateProjectDto;
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

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateProjectHandler {
  private final ProjectRepository projectRepository;
  private final ModuleTypeRepository moduleTypeRepository;
  private final DisciplineRepository disciplineRepository;
  private final AuthenticationFacade authenticationFacade;

  public Project handle(UUID projectId, UpdateProjectDto projectDto) {
    // TODO: Proper exception
    var project = projectRepository.findById(projectId).orElseThrow();

    // TODO: Authorization
    var partner = buildPartner(projectDto.partner());
    var context = buildContext(projectDto.context());
    var supervisors = buildSupervisors(projectDto.supervisors());
    var timeBox = buildTimeBox(projectDto.timeboxDto());

    project.setPartner(partner);
    project.setCurriculumContext(context);
    project.setTimeBox(timeBox);

    project.setTitle(projectDto.title());
    project.setDescription(projectDto.description());
    project.setSummary(projectDto.summary());
    project.setRequirement(projectDto.requirement());

    if (!supervisors.isEmpty()) {
      project.offer(supervisors);
    }

    return this.projectRepository.save(project);
  }

  private Partner buildPartner(PartnerDto partnerDto) {
    if (partnerDto == null) {
      return null;
    }
    return new Partner(partnerDto.organizationId());
  }

  private TimeBox buildTimeBox(TimeBoxDto dto) {
    if (dto == null) {
      return null;
    }
    return new TimeBox(dto.start(), dto.end());
  }

  private List<Supervisor> buildSupervisors(List<SupervisorDto> supervisorDtos) {
    return supervisorDtos.stream()
        .map(dto -> new Supervisor(dto.id()))
        .toList();
  }

  private CurriculumContext buildContext(CurriculumContextDto dtoContext) {
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
