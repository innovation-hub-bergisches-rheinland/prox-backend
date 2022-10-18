package de.innovationhub.prox.modules.project.infrastructure.persistence;

import de.innovationhub.prox.modules.commons.infrastructure.InfrastructureComponent;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplinePort;
import de.innovationhub.prox.modules.project.infrastructure.persistence.mapper.DisciplineMapper;
import de.innovationhub.prox.modules.project.infrastructure.persistence.model.query.QDisciplineEntity;
import java.util.List;
import java.util.Optional;

@InfrastructureComponent
public class EBeanDisciplinePort implements DisciplinePort {

  private static final QDisciplineEntity qDiscipline = new QDisciplineEntity();
  private static final DisciplineMapper disciplineMapper = DisciplineMapper.MAPPER;

  @Override
  public List<Discipline> getAll() {
    return qDiscipline.findList().stream().map(disciplineMapper::toDomain).toList();
  }

  @Override
  public Optional<Discipline> getByKey(String key) {
    return qDiscipline.key.eq(key).findOneOrEmpty().map(disciplineMapper::toDomain);
  }
}
