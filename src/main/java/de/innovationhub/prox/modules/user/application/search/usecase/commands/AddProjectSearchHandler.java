package de.innovationhub.prox.modules.user.application.search.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.search.dto.AddProjectSearchDto;
import de.innovationhub.prox.modules.user.domain.search.ProjectSearchEntry;
import de.innovationhub.prox.modules.user.domain.search.SearchHistoryRepository;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class AddProjectSearchHandler {

  private final SearchHistoryRepository searchHistoryRepository;

  @Transactional
  public void handle(UUID userId, AddProjectSearchDto projectSearch) {
    Objects.requireNonNull(userId);
    var searchHistory = searchHistoryRepository.findByUserIdOrCreate(userId);
    searchHistory.addSearch(ProjectSearchEntry.create(
        projectSearch.text(),
        projectSearch.tagIds(),
        projectSearch.states(),
        projectSearch.moduleTypeKeys(),
        projectSearch.disciplineKeys()
    ));

    searchHistoryRepository.save(searchHistory);
  }
}
