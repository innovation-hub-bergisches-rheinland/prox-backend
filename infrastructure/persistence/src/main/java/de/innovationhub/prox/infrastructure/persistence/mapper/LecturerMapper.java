package de.innovationhub.prox.infrastructure.persistence.mapper;

import de.innovationhub.prox.infrastructure.persistence.model.LecturerEntity;
import de.innovationhub.prox.profile.lecturer.Lecturer;
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
