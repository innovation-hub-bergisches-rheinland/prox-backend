package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectRequest;
import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectRequest.TimeBoxDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.CurriculumContextRequest;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import de.innovationhub.prox.modules.project.domain.project.Author;
import de.innovationhub.prox.modules.project.domain.project.CurriculumContext;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import de.innovationhub.prox.modules.project.domain.project.TimeBox;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class CreateProjectHandler {

  private final ProjectRepository projectRepository;
  private final ModuleTypeRepository moduleTypeRepository;
  private final DisciplineRepository disciplineRepository;
  private final AuthenticationFacade authenticationFacade;

  public Project handle(CreateProjectRequest projectDto) {
    var author = buildAuthor();
    var context = buildContext(projectDto.context());
    var timeBox = buildTimeBox(projectDto.timeBox());

    var project = Project.create(author,
        projectDto.title(),
        projectDto.summary(),
        projectDto.description(),
        projectDto.requirement(),
        context,
        timeBox,
        projectDto.partnerId()
    );

    if(projectDto.supervisors() != null && !projectDto.supervisors().isEmpty()) {
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

  private Author buildAuthor() {
    var authenticatedUser = authenticationFacade.currentAuthenticatedId();
    return new Author(authenticatedUser);
  }
}
