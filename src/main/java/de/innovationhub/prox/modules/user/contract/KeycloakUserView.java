package de.innovationhub.prox.modules.user.contract;

import java.io.Serializable;
import java.util.UUID;

public record KeycloakUserView(
    UUID id,
    String name
) implements Serializable {

}
