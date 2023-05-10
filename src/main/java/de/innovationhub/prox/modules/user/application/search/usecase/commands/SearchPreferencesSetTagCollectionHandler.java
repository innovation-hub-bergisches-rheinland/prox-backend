package de.innovationhub.prox.modules.user.application.search.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferencesRepository;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchPreferencesSetTagCollectionHandler {
  private final SearchPreferencesRepository searchPreferencesRepository;

  @Transactional
  public void handle(UUID userId, UUID tagCollectionId) {
    Objects.requireNonNull(userId);
    Objects.requireNonNull(tagCollectionId);

    var optSp = searchPreferencesRepository.findByUserId(userId);
    if(optSp.isEmpty()) return;
    var sp = optSp.get();
    sp.setTagCollection(tagCollectionId);

    searchPreferencesRepository.save(sp);
  }
}
