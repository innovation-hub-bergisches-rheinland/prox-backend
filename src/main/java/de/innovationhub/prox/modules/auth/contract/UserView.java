package de.innovationhub.prox.modules.auth.contract;

import java.io.Serializable;
import java.util.UUID;

public record UserView(
    UUID id,
    String name
) implements Serializable {

}
