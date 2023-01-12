package de.innovationhub.prox.modules.project.application.project.event;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.auth.contract.ProxUserStarredProjectIntegrationEvent;
import de.innovationhub.prox.modules.auth.contract.ProxUserUnstarredProjectIntegrationEvent;
import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.ProjectIntegrationTest;
import de.innovationhub.prox.modules.project.domain.project.InterestedUser;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

class StarIntegrationEventListenersTest extends ProjectIntegrationTest {
  @Autowired
  ApplicationEventPublisher eventPublisher;

  @Autowired
  ProjectRepository projectRepository;

  @Test
  void shouldAddInterestOnStar() {
    var project = ProjectFixtures.build_a_project();
    var userId = UUID.randomUUID();
    projectRepository.save(project);

    eventPublisher.publishEvent(new ProxUserStarredProjectIntegrationEvent(userId, project.getId()));

    var updatedProject = projectRepository.findById(project.getId()).get();
    assertThat(updatedProject.getInterestedUsers())
        .extracting(InterestedUser::getUserId)
        .contains(userId);
  }

  @Test
  void shouldRemoveInterestOnStar() {
    var project = ProjectFixtures.build_a_project();
    var userId = UUID.randomUUID();
    project.stateInterest(new InterestedUser(userId));
    projectRepository.save(project);

    eventPublisher.publishEvent(new ProxUserUnstarredProjectIntegrationEvent(userId, project.getId()));

    var updatedProject = projectRepository.findById(project.getId()).get();
    assertThat(updatedProject.getInterestedUsers())
        .extracting(InterestedUser::getUserId)
        .doesNotContain(userId);
  }
}