package de.innovationhub.prox.modules.recommendation.application.web;

import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse;
import de.innovationhub.prox.modules.recommendation.application.usecase.GetRecommendationsHandler;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
  private final GetRecommendationsHandler getRecommendations;

  @GetMapping
  public ResponseEntity<RecommendationResponse> getRecommendations(
      @NotNull
      @NotEmpty
      @RequestParam(required = true)
      List<UUID> seedTags,
      @NotNull
      @RequestParam(required = false, defaultValue = "")
      List<UUID> excludedIds,
      @RequestParam(required = false, defaultValue = "5")
      Integer limit
  ) {
    var request = new RecommendationRequest(seedTags, excludedIds, limit);
    return ResponseEntity.ok(getRecommendations.handle(request));
  }
}
