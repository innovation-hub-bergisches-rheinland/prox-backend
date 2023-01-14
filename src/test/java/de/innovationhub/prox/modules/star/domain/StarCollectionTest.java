package de.innovationhub.prox.modules.star.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.star.domain.event.ProjectStarred;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class StarCollectionTest {

  @Test
  void shouldAddStar() {
    var project = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    var collection = new StarCollection(userId);

    collection.starProject(project);

    assertThat(collection.getStarredProjects()).contains(project);
    assertThat(collection.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectStarred)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectStarred.class, event -> {
          assertThat(event.projectId()).isEqualTo(project);
          assertThat(event.userId()).isEqualTo(userId);
        });
  }

  @Test
  void shouldRemoveInterest() {
    var project = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    var collection = new StarCollection(userId);
    collection.starProject(project);


    collection.unstarProject(project);

    assertThat(collection.getStarredProjects()).doesNotContain(project);
    assertThat(collection.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectStarred)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectStarred.class, event -> {
          assertThat(event.projectId()).isEqualTo(project);
          assertThat(event.userId()).isEqualTo(userId);
        });
  }
}