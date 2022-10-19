package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDto;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;

@ApplicationComponent
public class FindOrCreateTagHandler implements UseCaseHandler<TagDto, FindOrCreateTag> {

  private final TagRepository tagRepository;
  private final TagDtoMapper tagDtoMapper;

  public FindOrCreateTagHandler(TagRepository tagRepository, TagDtoMapper tagDtoMapper) {
    this.tagRepository = tagRepository;
    this.tagDtoMapper = tagDtoMapper;
  }

  @Override
  public TagDto handle(FindOrCreateTag useCase) {
    var foundTag = tagRepository.getByTag(useCase.tag());
    if (foundTag.isPresent()) {
      return tagDtoMapper.toDto(foundTag.get());
    }

    var tag = Tag.createNew(useCase.tag());
    tagRepository.save(tag);
    return tagDtoMapper.toDto(tag);
  }
}
