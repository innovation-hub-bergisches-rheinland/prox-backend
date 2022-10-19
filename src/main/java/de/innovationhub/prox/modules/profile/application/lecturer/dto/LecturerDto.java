package de.innovationhub.prox.modules.profile.application.lecturer.dto;

import java.util.UUID;

public record LecturerDto(
    UUID id,
    UUID userId,
    String name
) { }
