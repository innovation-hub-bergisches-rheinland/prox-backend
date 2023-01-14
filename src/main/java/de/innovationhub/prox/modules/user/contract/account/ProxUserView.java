package de.innovationhub.prox.modules.user.contract.account;

import java.util.UUID;

public record ProxUserView(
    UUID id,
    String name,
    String email
) {

}
