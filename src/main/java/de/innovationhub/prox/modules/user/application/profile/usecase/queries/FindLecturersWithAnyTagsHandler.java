package de.innovationhub.prox.modules.user.application.profile.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FindLecturersWithAnyTagsHandler {

  private final UserProfileRepository userProfileRepository;

  public Page<UserProfile> handle(List<UUID> tags) {
    return userProfileRepository.findAllLecturersWithAnyTag(tags, Pageable.unpaged());
  }
}
