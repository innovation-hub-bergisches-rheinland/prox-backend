package de.innovationhub.prox.modules.tag.application.tag.dto;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagDtoMapper {
  TagDtoMapper INSTANCE = Mappers.getMapper(TagDtoMapper.class);

  TagDto toDto(Tag tag);
}