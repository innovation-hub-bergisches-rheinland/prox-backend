package de.innovationhub.prox.modules.user.application.account;

import de.innovationhub.prox.config.MessagingConfig;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.account.usecase.command.AssignProfessorGroup;
import java.util.List;
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
public class KeycloakVerifyEmailEventListener {

  private final AssignProfessorGroup assignGroup;
  private static final List<String> PROFESSOR_EMAIL_PATTERNS = List.of("@th-koeln.de",
      "@fh-koeln.de");

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

    var email = event.getDetails().get("email");
    if (email == null) {
      return;
    }

    if (PROFESSOR_EMAIL_PATTERNS.stream().noneMatch(email::endsWith)) {
      return;
    }

    assignGroup.handle(event.getUserId());
  }
}
