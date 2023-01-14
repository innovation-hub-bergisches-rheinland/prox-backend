package de.innovationhub.prox.modules.user.application.account.web.dto;

import java.util.UUID;

public record ProxUserDto(
    UUID id,
    String name
) {

}
