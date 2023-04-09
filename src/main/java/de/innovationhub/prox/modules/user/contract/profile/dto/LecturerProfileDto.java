package de.innovationhub.prox.modules.user.contract.profile.dto;

import de.innovationhub.prox.modules.user.application.profile.dto.LecturerProfileInformationDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LecturerProfile")
public record LecturerProfileDto(
    LecturerProfileInformationDto profile
) {

}
