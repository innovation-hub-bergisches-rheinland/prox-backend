package de.innovationhub.prox.modules.user.application.search.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.search.SearchHistory;
import de.innovationhub.prox.modules.user.domain.search.SearchHistoryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class GetSearchHistoryHandler {

  private final SearchHistoryRepository searchHistoryRepository;

  public Optional<SearchHistory> handle(UUID userId) {
    return searchHistoryRepository.findByUserId(userId);
  }
}
