package de.innovationhub.prox.modules.profile.infrastructure.persistence.mapper;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.infrastructure.persistence.model.LecturerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface LecturerMapper {

  LecturerMapper MAPPER = Mappers.getMapper(LecturerMapper.class);

  @Mapping(target = "id", source = "id")
  @Mapping(target = ".", source = "profile")
  LecturerEntity toPersistence(Lecturer lecturer);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "profile", source = ".")
  Lecturer toDomain(LecturerEntity entity);
}
