package de.innovationhub.prox.modules.profile.application.user.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.domain.user.User;
import de.innovationhub.prox.modules.profile.domain.user.UserPort;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class RegisterUserUseCaseHandler implements UseCaseHandler<User, RegisterUser> {

  private final UserPort userPort;

  @Override
  @Transactional
  public User handle(RegisterUser useCase) {
    var user = User.register(useCase.id(), useCase.name(), useCase.email());
    user = userPort.save(user);

    return user;
  }
}
