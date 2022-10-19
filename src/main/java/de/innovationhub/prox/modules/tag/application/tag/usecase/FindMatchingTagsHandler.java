package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDto;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.List;

@ApplicationComponent
public class FindMatchingTagsHandler implements UseCaseHandler<List<TagDto>, FindMatchingTags> {

  private final TagRepository tagRepository;
  private final TagDtoMapper tagDtoMapper;

  public FindMatchingTagsHandler(TagRepository tagRepository, TagDtoMapper tagDtoMapper) {
    this.tagRepository = tagRepository;
    this.tagDtoMapper = tagDtoMapper;
  }

  @Override
  public List<TagDto> handle(FindMatchingTags useCase) {
    return tagRepository.findMatching(useCase.partialTag())
        .stream()
        .map(tagDtoMapper::toDto)
        .toList();
  }
}
