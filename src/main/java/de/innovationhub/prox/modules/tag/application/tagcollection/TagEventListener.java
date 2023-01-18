package de.innovationhub.prox.modules.tag.application.tagcollection;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.contract.user.UserProfileTaggedIntegrationEvent;
import de.innovationhub.prox.modules.profile.contract.OrganizationTaggedIntegrationEvent;
import de.innovationhub.prox.modules.project.contract.ProjectTaggedIntegrationEvent;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.SetTagCollectionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class TagEventListener {
  private final SetTagCollectionHandler handler;

  @EventListener
  public void handleProjectTagged(ProjectTaggedIntegrationEvent event) {
    handler.handle(event.projectId(), event.tags());
  }

  @EventListener
  public void handleUserProfileTagged(UserProfileTaggedIntegrationEvent event) {
    handler.handle(event.profileId(), event.tags());
  }

  @EventListener
  public void handleOrganizationTagged(OrganizationTaggedIntegrationEvent event) {
    handler.handle(event.organizationId(), event.tags());
  }
}
