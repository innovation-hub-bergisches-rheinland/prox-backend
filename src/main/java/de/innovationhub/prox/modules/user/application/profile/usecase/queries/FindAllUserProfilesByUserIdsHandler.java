package de.innovationhub.prox.modules.user.application.profile.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindAllUserProfilesByUserIdsHandler {

  private final UserProfileRepository userProfileRepository;

  public List<UserProfile> handle(List<UUID> userIds) {
    Objects.requireNonNull(userIds);
    var result = new ArrayList<UserProfile>();
    for(UUID userId: userIds){
      var found = userProfileRepository.findByUserId(userId);
      found.ifPresent(result::add);
    }
    return result;
  }

}
