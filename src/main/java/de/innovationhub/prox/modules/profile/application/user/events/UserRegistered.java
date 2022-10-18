package de.innovationhub.prox.modules.profile.application.user.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.profile.domain.user.User;
import java.util.UUID;

public record UserRegistered(UUID id, String name, String email) implements Event {
  public static UserRegistered from(User user) {
    return new UserRegistered(user.getId(), user.getName(), user.getEmail());
  }
}
