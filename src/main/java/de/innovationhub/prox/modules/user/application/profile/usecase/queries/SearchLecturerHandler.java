package de.innovationhub.prox.modules.user.application.profile.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchLecturerHandler {

  private final UserProfileRepository userProfileRepository;

  public Page<UserProfile> handle(
      String query,
      Pageable pageable) {

    return userProfileRepository.searchLecturers(query, pageable);
  }
}
