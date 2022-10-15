package profile.user.usecase;

import commons.ApplicationComponent;
import commons.event.EventPublisher;
import commons.usecase.UseCaseHandler;
import de.innovationhub.prox.profile.user.User;
import de.innovationhub.prox.profile.user.UserPort;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import profile.user.events.UserRegistered;

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
