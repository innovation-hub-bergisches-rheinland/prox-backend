package de.innovationhub.prox.modules.recommendation.contract;

import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

public record RecommendationRequest(
    List<UUID> seedTags,
    List<UUID> excludedIds,
    @Min(1)
    Integer limit
) {

}
