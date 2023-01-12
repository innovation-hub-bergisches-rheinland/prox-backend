package de.innovationhub.prox.modules.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.auth.domain.event.ProxUserRegistered;
import de.innovationhub.prox.modules.auth.domain.event.ProxUserStarredProject;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProxUserTest {

  @Test
  void shouldRegister() {
    var user = ProxUser.register(UUID.randomUUID());

    assertThat(user.getDomainEvents())
        .filteredOn(event -> event instanceof ProxUserRegistered)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProxUserRegistered.class, event -> {
          assertThat(event.userId()).isEqualTo(user.getId());
        });
  }

  @Test
  void shouldAddStar() {
    var user = createTestUser();
    var project = UUID.randomUUID();

    user.starProject(project);

    assertThat(user.getStarredProjects()).contains(project);
    assertThat(user.getDomainEvents())
        .filteredOn(event -> event instanceof ProxUserStarredProject)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProxUserStarredProject.class, event -> {
          assertThat(event.projectId()).isEqualTo(project);
          assertThat(event.userId()).isEqualTo(user.getId());
        });
  }

  @Test
  void shouldRemoveInterest() {
    var user = createTestUser();
    var project = UUID.randomUUID();
    user.starProject(project);

    user.unstarProject(project);

    assertThat(user.getStarredProjects()).doesNotContain(project);
    assertThat(user.getDomainEvents())
        .filteredOn(event -> event instanceof ProxUserStarredProject)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProxUserStarredProject.class, event -> {
          assertThat(event.projectId()).isEqualTo(project);
          assertThat(event.userId()).isEqualTo(user.getId());
        });
  }

  private ProxUser createTestUser() {
    return ProxUser.register(UUID.randomUUID());
  }
}