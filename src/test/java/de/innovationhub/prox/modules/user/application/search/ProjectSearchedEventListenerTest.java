package de.innovationhub.prox.modules.user.application.search;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.project.contract.event.ProjectSearched;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.user.domain.search.SearchHistoryRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;

class ProjectSearchedEventListenerTest extends AbstractIntegrationTest {

  private final static String USER_ID_STR = "00000000-0000-0000-0000-000000000000";
  private final static UUID USER_ID = UUID.fromString(USER_ID_STR);

  @Autowired
  ApplicationEventPublisher eventPublisher;

  @Autowired
  SearchHistoryRepository searchHistoryRepository;

  @Test
  @WithMockUser(USER_ID_STR)
  void shouldSaveSearchOnEvent() {
    var event = new ProjectSearched("test", List.of(ProjectState.OFFERED), List.of("T1"),
        List.of("T2"), List.of(UUID.randomUUID()));

    eventPublisher.publishEvent(event);

    var result = searchHistoryRepository.findByUserId(USER_ID);
    assertThat(result)
        .get()
        .satisfies(r -> {
          assertThat(r.getProjectSearches()).hasSize(1);
          assertThat(r.getProjectSearches().get(0))
              .satisfies(search -> {
                assertThat(search.getText()).isEqualTo(event.text());
                assertThat(search.getDisciplines()).containsExactlyInAnyOrderElementsOf(
                    event.specializationKeys());
                assertThat(search.getModuleTypes()).containsExactlyInAnyOrderElementsOf(
                    event.moduleTypeKeys());
                assertThat(search.getStates()).containsExactlyInAnyOrderElementsOf(event.states());
                assertThat(search.getTags()).containsExactlyInAnyOrderElementsOf(event.tagIds());
              });
        });
  }
}