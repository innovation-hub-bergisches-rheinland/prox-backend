package de.innovationhub.prox.modules.profile.application.lecturer.dto;

import java.util.List;
import java.util.UUID;

public record LecturerDto(
    UUID id,
    UUID userId,
    String name,
    LecturerProfileDto profile,
    List<String> tags
) { }
