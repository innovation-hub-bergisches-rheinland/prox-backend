package de.innovationhub.prox.modules.recommendation.application.web;

import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse;
import de.innovationhub.prox.modules.recommendation.application.usecase.GetRecommendationsHandler;
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
      List<UUID> excludedIds
  ) {
    var request = new RecommendationRequest(seedTags, excludedIds);
    return ResponseEntity.ok(getRecommendations.handle(request));
  }
}
