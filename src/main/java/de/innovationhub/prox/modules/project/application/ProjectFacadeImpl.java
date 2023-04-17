package de.innovationhub.prox.modules.project.application;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindAllProjectByIdsHandler;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.project.application.project.dto.ProjectDtoAssembler;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindProjectByIdHandler;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class ProjectFacadeImpl implements ProjectFacade {
  private final FindAllProjectByIdsHandler findAllProjectByIdsHandler;
  private final FindProjectByIdHandler findProjectByIdHandler;
  private final ProjectDtoAssembler projectDtoAssembler;

  @Override
  public Optional<ProjectDto> get(UUID id) {
    return findProjectByIdHandler.handle(id)
        .map(projectDtoAssembler::toDto);
  }

  @Override
  public List<ProjectDto> findAllByIds(Collection<UUID> ids) {
    return findAllProjectByIdsHandler.handle(ids)
        .stream()
        .map(projectDtoAssembler::toDto)
        .toList();
  }
}
