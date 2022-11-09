package de.innovationhub.prox.modules.profile.application.lecturer.web.dto;

import java.util.List;
import java.util.UUID;

public record SetLecturerTagsRequestDto(
    List<UUID> tags
) {

}
