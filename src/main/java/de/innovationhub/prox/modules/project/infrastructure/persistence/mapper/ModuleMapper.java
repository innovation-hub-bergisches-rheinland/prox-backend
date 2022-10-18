package de.innovationhub.prox.modules.project.infrastructure.persistence.mapper;

import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.infrastructure.persistence.model.ModuleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ModuleMapper {

  ModuleMapper MAPPER = Mappers.getMapper(ModuleMapper.class);

  @Mapping(target = "key", source = "key")
  @Mapping(target = "active", source = "active")
  ModuleEntity toPersistence(ModuleType discipline);

  @Mapping(target = "key", source = "key")
  @Mapping(target = "active", source = "active")
  ModuleType toDomain(ModuleEntity entity);
}
