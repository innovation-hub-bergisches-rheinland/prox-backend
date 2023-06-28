package de.innovationhub.prox.modules.recommendation.application;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.recommendation.application.usecase.GetRecommendationsHandler;
import de.innovationhub.prox.modules.recommendation.contract.RecommendationFacade;
import de.innovationhub.prox.modules.recommendation.contract.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.contract.RecommendationResponse;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class RecommendationFacadeImpl implements RecommendationFacade {

  private final GetRecommendationsHandler getRecommendationsHandler;

  @Override
  public RecommendationResponse getRecommendations(RecommendationRequest request) {
    return getRecommendationsHandler.handle(request);
  }
}
