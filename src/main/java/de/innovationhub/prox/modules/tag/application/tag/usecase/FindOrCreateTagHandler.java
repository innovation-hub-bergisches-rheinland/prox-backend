package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDto;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagPort;

@ApplicationComponent
public class FindOrCreateTagHandler implements UseCaseHandler<TagDto, FindOrCreateTag> {

  private final TagPort tagPort;
  private final TagDtoMapper tagDtoMapper;

  public FindOrCreateTagHandler(TagPort tagPort, TagDtoMapper tagDtoMapper) {
    this.tagPort = tagPort;
    this.tagDtoMapper = tagDtoMapper;
  }

  @Override
  public TagDto handle(FindOrCreateTag useCase) {
    var foundTag = tagPort.getByTag(useCase.tag());
    if (foundTag.isPresent()) {
      return tagDtoMapper.toDto(foundTag.get());
    }

    var tag = Tag.createNew(useCase.tag());
    tagPort.save(tag);
    return tagDtoMapper.toDto(tag);
  }
}
