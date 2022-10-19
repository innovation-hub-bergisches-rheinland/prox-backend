package de.innovationhub.prox.modules.profile.application.user.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.event.EventPublisher;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.domain.user.User;
import de.innovationhub.prox.modules.profile.domain.user.UserPort;
import de.innovationhub.prox.modules.profile.domain.user.events.UserRegistered;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class RegisterUserUseCaseHandler implements UseCaseHandler<User, RegisterUser> {

  private final UserPort userPort;
  private final EventPublisher eventPublisher;

  @Override
  @Transactional
  public User handle(RegisterUser useCase) {
    var user = new User(useCase.id(), useCase.name(), useCase.email());
    user = userPort.save(user);

    eventPublisher.publish(UserRegistered.from(user));

    return user;
  }
}
