package de.innovationhub.prox.infrastructure.persistence.port;

import de.innovationhub.prox.commons.InfrastructureComponent;
import de.innovationhub.prox.infrastructure.persistence.mapper.DisciplineMapper;
import de.innovationhub.prox.infrastructure.persistence.model.query.QDisciplineEntity;
import de.innovationhub.prox.project.discipline.Discipline;
import de.innovationhub.prox.project.discipline.DisciplinePort;
import java.util.List;
import java.util.Optional;

@InfrastructureComponent
public class EBeanDisciplinePort implements DisciplinePort {

  private static final QDisciplineEntity qDiscipline = new QDisciplineEntity();
  private static final DisciplineMapper disciplineMapper = DisciplineMapper.MAPPER;

  @Override
  public List<Discipline> getAll() {
    return qDiscipline.findList()
      .stream()
      .map(disciplineMapper::toDomain)
      .toList();
  }

  @Override
  public Optional<Discipline> getByKey(String key) {
    return qDiscipline
      .key.eq(key)
      .findOneOrEmpty()
      .map(disciplineMapper::toDomain);
  }
}
