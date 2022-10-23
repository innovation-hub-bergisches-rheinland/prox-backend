package de.innovationhub.prox.modules.project.domain.supervisor.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.project.domain.supervisor.Supervisor;
import java.util.UUID;

public record SupervisorCreated(UUID id, String name) implements Event {
  public static SupervisorCreated from(Supervisor supervisor) {
    return new SupervisorCreated(supervisor.getId(), supervisor.getName());
  }
}
