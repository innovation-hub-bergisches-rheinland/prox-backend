package de.innovationhub.prox.modules.user.application.profile;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.config.MessagingConfig;
import de.innovationhub.prox.infra.keycloak.KeycloakClient;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto.ContactInformationRequestDto;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.CreateUserProfileHandler;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.events.Event;
import org.keycloak.events.EventType;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

@ApplicationComponent
@RequiredArgsConstructor
@Slf4j
public class KeycloakRegisterEventListener {

  private final CreateUserProfileHandler createUserProfile;
  private final KeycloakClient keycloakClient;

  @RabbitListener(bindings = {
      @QueueBinding(
          value = @Queue(value = MessagingConfig.REGISTER_QUEUE_NAME, arguments = {
              // TODO: Is it possible to define that globally?
              // TODO: We might refine the strategy here. E.g. we could use automatic requeing
              @Argument(name = MessagingConfig.X_DEAD_LETTER_EXCHANGE, value = MessagingConfig.ERROR_EXCHANGE_NAME)
          }),
          exchange = @Exchange(value = MessagingConfig.TOPIC_EXCHANGE_NAME, type = "topic"),
          key = MessagingConfig.REGISTER_KEY
      )
  })
  public void onRegisterEvent(@Payload Event event) {
    if (event.getType() != EventType.REGISTER) {
      throw new RuntimeException("Invalid resource type received: " + event.getType());
    }

    var firstName = event.getDetails().getOrDefault("first_name", "");
    var lastName = event.getDetails().getOrDefault("last_name", "");
    var email = event.getDetails().getOrDefault("email", "");
    var strId = event.getUserId();
    var id = UUID.fromString(strId);

    try {
      // We try resolving user information from the remote Keycloak instance
      // Reason for that is, that we get only valid event data (firstName/lastName) when the register
      // form is being used. If the user uses a broker like Microsoft OIDC, we don't get the data.
      // Hopefully the data is present in the remote Keycloak instance.
      // TODO: We should probably rethink the whole event handling here. It's not really nice.

      var userRepresentation = keycloakClient.getById(strId);
      if (userRepresentation.isPresent()) {
        var user = userRepresentation.get();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
      } else {
        log.warn(
            "User with id {} registered, but it could not be found remotely. Using data from the event...",
            strId);
      }
    } catch (Exception e) {
      log.warn("User with id {} registered, but it could not be found remotely. Using data from the event...", strId, e);
    }

    var displayName = firstName + " " + lastName;
    var dto = new CreateUserProfileRequestDto(displayName, "",
        new ContactInformationRequestDto(
            email,
            null,
            null
        ), false);
    createUserProfile.handle(id, dto);
  }
}
