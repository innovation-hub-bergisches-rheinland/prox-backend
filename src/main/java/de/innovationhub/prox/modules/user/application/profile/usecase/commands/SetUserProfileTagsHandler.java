package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetUserProfileTagsHandler {

  private final UserProfileRepository userProfileRepository;

  @Transactional
  public List<UUID> handle(UUID userId,
      List<UUID> tags) {
    var lecturer = userProfileRepository.findByUserId(userId).orElseThrow();

    lecturer.tagProfile(tags);
    userProfileRepository.save(lecturer);
    return List.copyOf(lecturer.getTags());
  }
}
