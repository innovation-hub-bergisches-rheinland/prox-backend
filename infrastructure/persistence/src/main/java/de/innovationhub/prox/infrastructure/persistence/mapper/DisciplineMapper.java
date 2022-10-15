package de.innovationhub.prox.infrastructure.persistence.mapper;

import de.innovationhub.prox.infrastructure.persistence.model.DisciplineEntity;
import de.innovationhub.prox.project.discipline.Discipline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DisciplineMapper {

  DisciplineMapper MAPPER = Mappers.getMapper(DisciplineMapper.class);

  @Mapping(target = "key", source = "key")
  DisciplineEntity toPersistence(Discipline discipline);

  @Mapping(target = "key", source = "key")
  Discipline toDomain(DisciplineEntity entity);
}
