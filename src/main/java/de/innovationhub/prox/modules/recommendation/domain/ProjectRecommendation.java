package de.innovationhub.prox.modules.recommendation.domain;

import java.util.Set;
import java.util.UUID;

public record ProjectRecommendation(
    UUID id,
    Set<UUID> supervisors,
    UUID partner,
    Set<UUID> tags
) {

}
