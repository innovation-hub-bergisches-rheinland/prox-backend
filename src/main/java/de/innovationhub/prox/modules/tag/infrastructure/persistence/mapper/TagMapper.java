package de.innovationhub.prox.modules.tag.infrastructure.persistence.mapper;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.infrastructure.persistence.model.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TagMapper {

  TagMapper MAPPER = Mappers.getMapper(TagMapper.class);

  @Mapping(target = "id", source = "id")
  TagEntity toPersistence(Tag tag);

  @Mapping(target = "id", source = "id")
  Tag toDomain(TagEntity entity);
}
