package de.innovationhub.prox.modules.tag.application.tagcollection.dto;

import de.innovationhub.prox.modules.tag.application.tag.dto.SynchronizeTagsResponse;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TagDtoMapper.class})
public interface TagCollectionDtoMapper {

  TagCollectionDtoMapper INSTANCE = Mappers.getMapper(TagCollectionDtoMapper.class);

  TagCollectionDto toDto(TagCollection tagCollection);
}
