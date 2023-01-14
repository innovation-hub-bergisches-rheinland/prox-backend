package de.innovationhub.prox.modules.user.application.lecturer.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(name = "Lecturer")
public record LecturerDto(
    UUID id,
    UUID userId,
    String name,
    boolean visibleInPublicSearch,
    LecturerProfileDto profile,
    List<String> tags,
    String avatarUrl,
    @JsonProperty("_permissions")
    LecturerPermissions permissions,

    Instant createdAt,
    Instant modifiedAt
) { }
