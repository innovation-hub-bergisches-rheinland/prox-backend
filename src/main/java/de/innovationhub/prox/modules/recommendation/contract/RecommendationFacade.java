package de.innovationhub.prox.modules.recommendation.contract;

public interface RecommendationFacade {

  RecommendationResponse getRecommendations(final RecommendationRequest request);
}
