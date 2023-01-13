package de.innovationhub.prox.modules.user.application.user;

import de.innovationhub.prox.config.MessagingConfig;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.user.usecase.command.AssignProfessorGroup;
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
public class KeycloakEventListener {

  private final AssignProfessorGroup assignGroup;

  @RabbitListener(bindings = {
      @QueueBinding(
          value = @Queue(value = MessagingConfig.VERIFY_EMAIL_QUEUE_NAME, arguments = {
              // TODO: Is it possible to define that globally?
              // TODO: We might refine the strategy here. E.g. we could use automatic requeing
              @Argument(name = MessagingConfig.X_DEAD_LETTER_EXCHANGE, value = MessagingConfig.ERROR_EXCHANGE_NAME)
          }),
          exchange = @Exchange(value = MessagingConfig.TOPIC_EXCHANGE_NAME, type = "topic"),
          key = MessagingConfig.VERIFY_EMAIL_KEY
      )
  })
  public void handleGroupAdded(@Payload Event event) {
    if (event.getType() != EventType.VERIFY_EMAIL) {
      throw new RuntimeException("Invalid resource type received: " + event.getType());
    }

    assignGroup.handle(event.getUserId());
  }
}
