package de.innovationhub.prox.infrastructure.persistence.port;

import de.innovationhub.prox.commons.InfrastructureComponent;
import de.innovationhub.prox.infrastructure.persistence.mapper.ModuleMapper;
import de.innovationhub.prox.infrastructure.persistence.model.query.QModuleEntity;
import de.innovationhub.prox.project.module.ModuleType;
import de.innovationhub.prox.project.module.ModuleTypePort;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@InfrastructureComponent
public class EBeanModulePort implements ModuleTypePort {

  private static final QModuleEntity qModule = new QModuleEntity();
  private static final ModuleMapper moduleMapper = ModuleMapper.MAPPER;

  @Override
  public List<ModuleType> getAll() {
    return qModule.findList()
      .stream()
      .map(moduleMapper::toDomain)
      .toList();
  }

  @Override
  public List<ModuleType> getByDisciplines(Collection<String> disciplineKeys) {
    return qModule
      .disciplines.isIn(List.copyOf(disciplineKeys))
      .findList()
      .stream()
      .map(moduleMapper::toDomain)
      .toList();
  }

  @Override
  public Optional<ModuleType> getByKey(String key) {
    return qModule
      .key.eq(key)
      .findOneOrEmpty()
      .map(moduleMapper::toDomain);
  }
}
