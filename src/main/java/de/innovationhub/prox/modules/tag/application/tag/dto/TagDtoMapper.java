package de.innovationhub.prox.modules.tag.application.tag.dto;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
@ApplicationComponent
public abstract class TagDtoMapper {

  public static final TagDtoMapper INSTANCE = Mappers.getMapper(TagDtoMapper.class);

  @Autowired
  private TagCollectionRepository tagCollectionRepository;

  @Mapping(target = "count", source = "tag", qualifiedByName = "tagCount")
  public abstract TagDto toDto(Tag tag);

  public abstract List<TagDto> toDtoList(List<Tag> tags);

  public SynchronizeTagsResponse toSynchronizeTagsResponse(List<Tag> tags) {
    return new SynchronizeTagsResponse(toDtoList(tags));
  }

  @Named("tagCount")
  protected long getTagCount(Tag tag) {
    return tagCollectionRepository.countTagUsage(tag.getId());
  }
}
