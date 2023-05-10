package de.innovationhub.prox.modules.project.application;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.discipline.dto.DisciplineMapper;
import de.innovationhub.prox.modules.project.application.discipline.usecase.queries.FindDisciplinesByKeyInHandler;
import de.innovationhub.prox.modules.project.application.module.dto.ModuleTypeMapper;
import de.innovationhub.prox.modules.project.application.module.usecase.queries.FindModuleTypesByKeyInHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindAllProjectByIdsHandler;
import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.contract.dto.ModuleTypeDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.project.application.project.dto.ProjectDtoAssembler;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindProjectByIdHandler;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
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
  private final FindDisciplinesByKeyInHandler findDisciplinesByKeyInHandler;
  private final FindModuleTypesByKeyInHandler findModuleTypesByKeyInHandler;
  private final DisciplineMapper disciplineMapper;
  private final ModuleTypeMapper moduleTypeMapper;

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

  @Override
  public List<DisciplineDto> findDisciplinesByKeyIn(Collection<String> keys) {
    return findDisciplinesByKeyInHandler.handle(keys)
        .stream()
        .map(disciplineMapper::toDto)
        .toList();
  }

  @Override
  public List<ModuleTypeDto> findModuleTypesByKeyIn(Collection<String> keys) {
    return findModuleTypesByKeyInHandler.handle(keys)
        .stream()
        .map(moduleTypeMapper::toDto)
        .toList();
  }
}
