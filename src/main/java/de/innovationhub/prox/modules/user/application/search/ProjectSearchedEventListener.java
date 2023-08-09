package de.innovationhub.prox.modules.user.application.search;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.contract.event.ProjectSearched;
import de.innovationhub.prox.modules.user.application.search.dto.AddProjectSearchDto;
import de.innovationhub.prox.modules.user.application.search.usecase.commands.AddProjectSearchHandler;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class ProjectSearchedEventListener {

  private final AddProjectSearchHandler addProjectSearchHandler;
  private final AuthenticationFacade authenticationFacade;

  @EventListener(ProjectSearched.class)
  public void handleEvent(ProjectSearched event) {
    if (authenticationFacade.isAuthenticated()) {
      var id = authenticationFacade.currentAuthenticatedId();
      addProjectSearchHandler.handle(id,
          new AddProjectSearchDto(event.text(), event.states(), event.specializationKeys(),
              event.moduleTypeKeys(), event.tagIds()));
    }
  }
}
