package de.innovationhub.prox.modules.recommendation.application.dto;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RecommendationRequest(
    List<UUID> seedTags,
    List<UUID> excludedIds
) {
}
