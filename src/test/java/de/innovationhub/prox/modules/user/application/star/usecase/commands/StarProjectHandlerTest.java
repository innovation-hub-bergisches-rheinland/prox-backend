package de.innovationhub.prox.modules.user.application.star.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.user.domain.star.StarCollection;
import de.innovationhub.prox.modules.user.domain.star.StarCollectionRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class StarProjectHandlerTest {
  StarCollectionRepository starCollectionRepository = mock(StarCollectionRepository.class);
  StarProjectHandler handler = new StarProjectHandler(starCollectionRepository);

  @Test
  void shouldCreateNewCollectionWhenNotFound() {
    var userId = UUID.randomUUID();
    var projectId = UUID.randomUUID();
    when(starCollectionRepository.findByUserId(userId)).thenReturn(Optional.empty());

    handler.handle(userId, projectId);

    var captor = ArgumentCaptor.forClass(StarCollection.class);
    verify(starCollectionRepository).save(captor.capture());
    assertThat(captor.getValue())
        .satisfies(collection -> {
          assertThat(collection.getUserId()).isEqualTo(userId);
          assertThat(collection.getStarredProjects()).contains(projectId);
        });
  }
}