package de.innovationhub.prox.modules.user.contract.user;

import java.util.UUID;

public record ProxUserView(
    UUID id,
    String name,
    String email
) {

}
