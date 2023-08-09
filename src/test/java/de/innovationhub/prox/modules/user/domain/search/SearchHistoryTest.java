package de.innovationhub.prox.modules.user.domain.search;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SearchHistoryTest {

  @Test
  void shouldRemoveSearchesWhenLimitIsPassed() {
    var searchHistory = dummySearchHistory();
    int ROLLOVER_LIMIT = 10;
    for (int i = 0; i <= ROLLOVER_LIMIT; i++) {
      searchHistory.addSearch(dummyProjectSearchEntry("%d".formatted(i)));
    }

    assertThat(searchHistory.getProjectSearches()).hasSize(ROLLOVER_LIMIT);
  }

  @Test
  void shouldReplaceEqualSearches() {
    var searchHistory = dummySearchHistory();
    searchHistory.addSearch(dummyProjectSearchEntry("1"));
    searchHistory.addSearch(dummyProjectSearchEntry("1"));

    assertThat(searchHistory.getProjectSearches()).hasSize(1);
  }

  private ProjectSearchEntry dummyProjectSearchEntry(String text) {
    return ProjectSearchEntry.create(text, List.of(),
        List.of(ProjectState.OFFERED), List.of("TE"), List.of("TE"));
  }

  private SearchHistory dummySearchHistory() {
    return SearchHistory.create(UUID.randomUUID());
  }
}