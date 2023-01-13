package de.innovationhub.prox.modules.profile.application.lecturer.event.incoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.keycloak.events.admin.AdminEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakAdminEventMq extends AdminEvent {
}
