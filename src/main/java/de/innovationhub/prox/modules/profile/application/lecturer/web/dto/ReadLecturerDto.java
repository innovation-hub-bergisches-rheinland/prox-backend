package de.innovationhub.prox.modules.profile.application.lecturer.web.dto;

import java.util.List;
import java.util.UUID;

public record ReadLecturerDto(
    UUID id,
    UUID userId,
    String name,
    LecturerProfileDto profile,
    List<String> tags,
    String avatarUrl
) { }
