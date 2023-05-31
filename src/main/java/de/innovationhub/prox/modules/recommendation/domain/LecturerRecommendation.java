package de.innovationhub.prox.modules.recommendation.domain;

import java.util.Set;
import java.util.UUID;

public record LecturerRecommendation(
    UUID id,
    Set<UUID> tags
) {

}
