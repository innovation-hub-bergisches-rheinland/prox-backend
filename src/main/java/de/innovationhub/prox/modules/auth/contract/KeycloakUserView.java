package de.innovationhub.prox.modules.auth.contract;

import java.io.Serializable;
import java.util.UUID;

public record KeycloakUserView(
    UUID id,
    String name
) implements Serializable {

}
