package de.innovationhub.prox.modules.user.application.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "LecturerProfileInformation")
public record LecturerProfileInformationDto(
    String affiliation,
    String subject,
    List<String> publications,
    String room,
    String consultationHour,
    String collegePage
) {
}
