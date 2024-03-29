package de.innovationhub.prox.modules.user.application.profile.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchUserHandler {

  private final UserProfileRepository userProfileRepository;

  public Page<UserProfile> handle(String searchQuery, Pageable pageable) {
    Objects.requireNonNull(searchQuery);
    return userProfileRepository.search(searchQuery, pageable);
  }
}
