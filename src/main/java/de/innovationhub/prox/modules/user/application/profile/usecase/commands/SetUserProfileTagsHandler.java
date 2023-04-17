package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetUserProfileTagsHandler {

  private final UserProfileRepository userProfileRepository;
  private final TagCollectionFacade tagCollectionFacade;

  @Transactional
  public List<TagDto> handle(UUID userId,
      List<UUID> tags) {
    var lecturer = userProfileRepository.findByUserId(userId).orElseThrow();
    var tc = tagCollectionFacade.setTagCollection(lecturer.getTagCollectionId(), tags);

    lecturer.setTagCollectionId(tc.id());
    userProfileRepository.save(lecturer);
    return tc.tags();
  }
}
