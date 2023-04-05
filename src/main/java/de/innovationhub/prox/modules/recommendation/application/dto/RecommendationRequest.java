package de.innovationhub.prox.modules.recommendation.application.dto;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record RecommendationRequest(
    @NotNull
    @NotEmpty
    List<UUID> seedTags
) {

}
