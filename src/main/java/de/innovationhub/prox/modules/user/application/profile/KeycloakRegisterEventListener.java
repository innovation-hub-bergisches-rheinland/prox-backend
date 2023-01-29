package de.innovationhub.prox.modules.user.application.profile;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.config.MessagingConfig;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto.ContactInformationRequestDto;
import de.innovationhub.prox.modules.user.application.profile.usecase.commands.CreateUserProfileHandler;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
public class KeycloakRegisterEventListener {

  private final CreateUserProfileHandler createUserProfile;

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
    // At the moment I don't feel very comfortable with the email as private information being
    // persisted by default. We might want to change this in the future.
    // var email = event.getDetails().getOrDefault("email", "");
    var id = UUID.fromString(event.getUserId());

    var dto = new CreateUserProfileRequestDto(firstName + " " + lastName, "",
        new ContactInformationRequestDto(
            null,
            null,
            null
        ), false);
    createUserProfile.handle(id, dto);
  }
}
