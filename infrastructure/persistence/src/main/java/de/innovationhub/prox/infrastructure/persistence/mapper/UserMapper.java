package de.innovationhub.prox.infrastructure.persistence.mapper;

import de.innovationhub.prox.infrastructure.persistence.model.UserEntity;
import de.innovationhub.prox.profile.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

  UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "id", source = "id")
  UserEntity toPersistence(User user);

  @Mapping(target = "id", source = "id")
  User toDomain(UserEntity entity);
}
