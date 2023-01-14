package de.innovationhub.prox.modules.user.application.account.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.account.ProxUserAccount;
import de.innovationhub.prox.modules.user.domain.account.ProxUserAccountRepository;
import de.innovationhub.prox.modules.user.domain.account.UserRoles;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchUserHandler {

  private final ProxUserAccountRepository proxUserAccountRepository;
  private static final List<String> ALLOWED_ROLES = List.of(UserRoles.LECTURER);

  public List<ProxUserAccount> handle(String searchQuery, String role) {
    Objects.requireNonNull(searchQuery);
    if (searchQuery.length() < 2) {
      throw new IllegalArgumentException("Search query must be at least 2 characters long");
    }

    if (role == null) {
      return proxUserAccountRepository.search(searchQuery);
    }

    if (!ALLOWED_ROLES.contains(role)) {
      throw new IllegalArgumentException("Role must be one of " + ALLOWED_ROLES);
    }

    return proxUserAccountRepository.searchWithRole(searchQuery, role);
  }
}
