package de.innovationhub.prox.modules.profile.application.lecturer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.innovationhub.prox.config.MessagingConfig;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.lecturer.event.incoming.KeycloakAdminEventMq;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands.CreateLecturerHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto.CreateLecturerProfileDto;
import de.innovationhub.prox.modules.user.contract.user.UserFacade;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

@ApplicationComponent
@RequiredArgsConstructor
public class KeycloakEventListener {

  private final CreateLecturerHandler createLecturer;
  private final UserFacade userFacade;
  private final ObjectMapper objectMapper;

  @RabbitListener(bindings = {
      @QueueBinding(
          value = @Queue(value = MessagingConfig.GROUP_ADDED_QUEUE_NAME),
          exchange = @Exchange(value = MessagingConfig.TOPIC_EXCHANGE_NAME, type = "topic"),
          key = MessagingConfig.CREATE_GROUP_MEMBERSHIP_KEY
      )
  })
  public void handleGroupAdded(@Payload KeycloakAdminEventMq event) throws JsonProcessingException {
    if (event.getResourceType() != ResourceType.GROUP_MEMBERSHIP) {
      throw new RuntimeException("Invalid resource type received: " + event.getResourceType());
    }
    if (event.getOperationType() != OperationType.CREATE) {
      return;
    }

    var representation = objectMapper.readValue(event.getRepresentation(),
        GroupRepresentation.class);
    if (!representation.getName().equals("professor")) {
      return;
    }

    // Resource path is "users/01db981f-cde5-4394-ba27-b48f61f10382/groups/8faa870a-00da-4ac6-9768-84999a79da27"
    var pathParts = event.getResourcePath().split("/");
    var userId = UUID.fromString(pathParts[1]);

    var user = userFacade.findById(userId).orElseThrow();
    var request = new CreateLecturerRequestDto(
        user.name(),
        new CreateLecturerProfileDto(null, null, null, null, null, null, user.email(), null, null,
            null),
        false
    );
    createLecturer.handle(userId, request);
  }
}
