package de.innovationhub.prox.modules.user.contract.account;

import java.util.UUID;

public record ProxUserAccountView(
    UUID id,
    String name,
    String email
) {

}
