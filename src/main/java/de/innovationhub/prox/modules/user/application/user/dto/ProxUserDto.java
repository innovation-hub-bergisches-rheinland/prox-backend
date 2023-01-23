package de.innovationhub.prox.modules.user.application.user.dto;

import java.util.UUID;

public record ProxUserDto(
    UUID id,
    String name
) {

}
