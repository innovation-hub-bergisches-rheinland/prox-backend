package de.innovationhub.prox.modules.auth.contract;

import java.util.UUID;

public record UserView(
    UUID id,
    String name
) {

}
