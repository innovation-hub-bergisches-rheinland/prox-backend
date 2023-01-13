package de.innovationhub.prox.modules.user.application.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.contract.KeycloakUserFacade;
import de.innovationhub.prox.modules.user.contract.KeycloakUserView;
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