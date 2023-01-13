package de.innovationhub.prox.modules.auth.application.usecase.queries;

import de.innovationhub.prox.modules.auth.contract.KeycloakUserFacade;
import de.innovationhub.prox.modules.auth.contract.KeycloakUserView;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchUserHandler {
  private final KeycloakUserFacade keycloakUserFacade;

  public List<KeycloakUserView> handle(String searchQuery) {
    return keycloakUserFacade.search(searchQuery);
  }
}
