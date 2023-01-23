package de.innovationhub.prox.modules.tag.application.tag.dto;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagDtoMapper {

  TagDtoMapper INSTANCE = Mappers.getMapper(TagDtoMapper.class);

  @Mapping(target = "tag", source = "tagName")
  TagDto toDto(Tag tag);

  List<TagDto> toDtoList(List<Tag> tags);

  default SynchronizeTagsResponse toSynchronizeTagsResponse(List<Tag> tags) {
    return new SynchronizeTagsResponse(toDtoList(tags));
  }
}
