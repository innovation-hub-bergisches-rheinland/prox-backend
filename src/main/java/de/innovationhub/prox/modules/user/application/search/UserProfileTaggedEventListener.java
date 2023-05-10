package de.innovationhub.prox.modules.user.application.search;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.search.usecase.commands.SearchPreferencesSetTagCollectionHandler;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileTagCollectionUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.event.TransactionalEventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class UserProfileTaggedEventListener {
  private final SearchPreferencesSetTagCollectionHandler searchPreferencesSetTagCollectionHandler;

  @EventListener(value = UserProfileTagCollectionUpdated.class)
  public void handle(UserProfileTagCollectionUpdated event) {
    searchPreferencesSetTagCollectionHandler.handle(event.userId(), event.tagCollectionId());
  }
}
