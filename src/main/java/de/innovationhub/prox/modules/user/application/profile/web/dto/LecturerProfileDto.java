package de.innovationhub.prox.modules.user.application.profile.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "LecturerProfile")
public record LecturerProfileDto(
    boolean visibleInPublicSearch,
    LecturerProfileInformationDto profile
) {

}
