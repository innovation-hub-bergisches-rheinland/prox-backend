package de.innovationhub.prox.modules.recommendation.application.web;

import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse;
import de.innovationhub.prox.modules.recommendation.application.usecase.GetProjectRecommendationsHandler;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecommendationController {

  private final GetProjectRecommendationsHandler getProjectRecommendationsHandler;

  @RequestMapping(value = "projects/{id}/recommendations", produces = "application/json")
  public ResponseEntity<RecommendationResponse> getRecommendation(@PathVariable("id") UUID id) {
    var recommendations = getProjectRecommendationsHandler.handle(id);
    return ResponseEntity.ok(recommendations);
  }
}
