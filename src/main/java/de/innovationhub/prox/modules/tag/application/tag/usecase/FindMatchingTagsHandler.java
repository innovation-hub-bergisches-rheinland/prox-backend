package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDto;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.domain.tag.TagPort;
import java.util.List;

@ApplicationComponent
public class FindMatchingTagsHandler implements UseCaseHandler<List<TagDto>, FindMatchingTags> {

  private final TagPort tagPort;
  private final TagDtoMapper tagDtoMapper;

  public FindMatchingTagsHandler(TagPort tagPort, TagDtoMapper tagDtoMapper) {
    this.tagPort = tagPort;
    this.tagDtoMapper = tagDtoMapper;
  }

  @Override
  public List<TagDto> handle(FindMatchingTags useCase) {
    return tagPort.findMatching(useCase.partialTag())
        .stream()
        .map(tagDtoMapper::toDto)
        .toList();
  }
}
