package de.innovationhub.prox.modules.user.application.account.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.account.ProxUserAccount;
import de.innovationhub.prox.modules.user.domain.account.ProxUserAccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchUserHandler {

  private final ProxUserAccountRepository proxUserAccountRepository;

  public List<ProxUserAccount> handle(String searchQuery) {
    return proxUserAccountRepository.search(searchQuery);
  }
}
