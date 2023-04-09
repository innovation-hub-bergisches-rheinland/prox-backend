package de.innovationhub.prox.modules.tag.application.tagcollection;

import static org.mockito.Mockito.verify;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.organization.contract.event.OrganizationTaggedIntegrationEvent;
import de.innovationhub.prox.modules.project.contract.event.ProjectTaggedIntegrationEvent;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.SetTagCollectionHandler;
import de.innovationhub.prox.modules.user.contract.profile.event.UserProfileTaggedIntegrationEvent;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

class TagEventListenerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  ApplicationEventPublisher eventPublisher;

  // The handler does the logic, we're just testing whether the handler is called from an event.
  // We test the logic in the handler itself
  @MockBean
  SetTagCollectionHandler handler;

  // Tests could be ParameterizedTests, but It would be more work to verify the correct arguments
  //  as the events do not share a common interface
  @Test
  void shouldCreateNewTagCollectionOnProjectTaggedEvent() {
    var event = new ProjectTaggedIntegrationEvent(UUID.randomUUID(), List.of(UUID.randomUUID(), UUID.randomUUID()));

    eventPublisher.publishEvent(event);

    verify(handler).handle(event.projectId(), event.tags());
  }

  @Test
  void shouldCreateNewTagCollectionOnLecturerTaggedEvent() {
    var event = new UserProfileTaggedIntegrationEvent(UUID.randomUUID(), List.of(UUID.randomUUID(), UUID.randomUUID()));

    eventPublisher.publishEvent(event);

    verify(handler).handle(event.profileId(), event.tags());
  }

  @Test
  void shouldCreateNewTagCollectionOnOrganizationTaggedEvent() {
    var event = new OrganizationTaggedIntegrationEvent(UUID.randomUUID(), List.of(UUID.randomUUID(), UUID.randomUUID()));

    eventPublisher.publishEvent(event);

    verify(handler).handle(event.organizationId(), event.tags());
  }
}