package de.innovationhub.prox.modules.project.application.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "SetPartnerRequest")
public record SetPartnerRequestDto(
    UUID organizationId
) {

}
