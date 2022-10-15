package de.innovationhub.prox.infrastructure.persistence.mapper;

import de.innovationhub.prox.infrastructure.persistence.model.TagEntity;
import de.innovationhub.prox.tag.Tag;
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
