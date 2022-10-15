package de.innovationhub.prox.infrastructure.persistence.port;

import de.innovationhub.prox.commons.InfrastructureComponent;
import de.innovationhub.prox.infrastructure.persistence.mapper.ProjectMapper;
import de.innovationhub.prox.infrastructure.persistence.model.query.QProjectEntity;
import de.innovationhub.prox.project.project.Project;
import de.innovationhub.prox.project.project.ProjectPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@InfrastructureComponent
public class EBeanProjectPort implements ProjectPort {

  private static final QProjectEntity qProject = new QProjectEntity();
  private static final ProjectMapper projectMapper = ProjectMapper.MAPPER;

  @Override
  public Project save(Project project) {
    var entity = projectMapper.toPersistence(project);
    entity.save();
    return projectMapper.toDomain(entity);
  }

  @Override
  public void delete(Project project) {
    var entity = projectMapper.toPersistence(project);
    entity.delete();
  }

  @Override
  public Optional<Project> getById(UUID id) {
    return qProject
      .id.eq(id)
      .findOneOrEmpty()
      .map(projectMapper::toDomain);
  }

  @Override
  public List<Project> getAll() {
    return qProject
      .findList()
      .stream()
      .map(projectMapper::toDomain)
      .toList();
  }
}
