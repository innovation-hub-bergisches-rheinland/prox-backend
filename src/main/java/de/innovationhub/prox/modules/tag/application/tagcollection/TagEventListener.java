package de.innovationhub.prox.modules.tag.application.tagcollection;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationTagged;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerTagged;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectTagged;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.SetTagCollectionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class TagEventListener {
  private final SetTagCollectionHandler handler;

  @EventListener
  public void handleProjectTagged(ProjectTagged event) {
    handler.handle(event.projectId(), event.tags());
  }

  @EventListener
  public void handleLecturerTagged(LecturerTagged event) {
    handler.handle(event.lecturerId(), event.tags());
  }

  @EventListener
  public void handleOrganizationTagged(OrganizationTagged event) {
    handler.handle(event.organizationId(), event.tags());
  }
}
