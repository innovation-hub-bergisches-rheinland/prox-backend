package de.innovationhub.prox.modules.user.application.user.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.user.ProxUser;
import de.innovationhub.prox.modules.user.domain.user.ProxUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchUserHandler {

  private final ProxUserRepository proxUserRepository;

  public List<ProxUser> handle(String searchQuery) {
    return proxUserRepository.search(searchQuery);
  }
}
