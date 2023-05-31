package de.innovationhub.prox.modules.recommendation.domain;

import de.innovationhub.prox.modules.recommendation.domain.calc.OverlapCoefficientCalculator;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ConfidenceScoreCalculator {
  private static final double LECTURER_PROFILE_MATCH_BOOST_FACTOR = 2.5;
  private static final double ORGANISATION_PROFILE_MATCH_BOOST_FACTOR = 2.5;
  private static final double PROJECT_MATCH_BOOST_FACTOR = 1;
  private static final OverlapCoefficientCalculator overlapCoefficientCalculator =
      new OverlapCoefficientCalculator();

  private final List<ProjectRecommendation> projectRecommendations;
  private final List<LecturerRecommendation> lecturerRecommendations;
  private final List<OrganizationRecommendation> organizationRecommendations;
  private final List<UUID> seedTags;

  Map<UUID, Double> lecturerConfidenceScores = null;
  Map<UUID, Double> organizationConfidenceScores = null;
  Map<UUID, Double> projectConfidenceScores = null;

  public ConfidenceScoreCalculator(List<ProjectRecommendation> projectRecommendations,
      List<LecturerRecommendation> lecturerRecommendations,
      List<OrganizationRecommendation> organizationRecommendations, List<UUID> seedTags) {
    this.projectRecommendations = projectRecommendations;
    this.lecturerRecommendations = lecturerRecommendations;
    this.organizationRecommendations = organizationRecommendations;
    this.seedTags = seedTags;
  }

  public Map<UUID, Double> getLecturerConfidenceScores() {
    if(lecturerConfidenceScores == null) {
      calculateLecturerConfidenceScores();
    }
    return Map.copyOf(lecturerConfidenceScores);
  }

  public Map<UUID, Double> getOrganizationConfidenceScores() {
    if(organizationConfidenceScores == null) {
      calculateOrganizationConfidenceScores();
    }
    return Map.copyOf(organizationConfidenceScores);
  }

  public Map<UUID, Double> getProjectConfidenceScores() {
    if(projectConfidenceScores == null) {
      calculateProjectConfidenceScores();
    }
    return Map.copyOf(projectConfidenceScores);
  }

  private void calculateLecturerConfidenceScores() {
    this.lecturerConfidenceScores = new HashMap<>();
    for (var supervisor : this.lecturerRecommendations) {
      final var score =
          LECTURER_PROFILE_MATCH_BOOST_FACTOR * overlapCoefficientCalculator.calculate(seedTags, supervisor.tags());
      addConfidenceScore(this.lecturerConfidenceScores, supervisor.id(), score, 1);
    }

    var projectScores = this.getProjectConfidenceScores();
    if(projectScores == null || projectScores.isEmpty()) {
      return;
    }

    for (var entry : projectScores.entrySet()) {
      var project = projectRecommendations.stream().filter(p -> p.id().equals(entry.getKey())).findFirst()
          .orElse(null);
      if (project == null) {
        continue;
      }

      var score = entry.getValue();
      var supervisors = project.supervisors();
      if (score > 0.0 && supervisors != null) {
        for (var supervisor : supervisors) {
          addConfidenceScore(this.lecturerConfidenceScores, supervisor, score, 2.0);
        }
      }
    }
  }

  private void calculateOrganizationConfidenceScores() {
    this.organizationConfidenceScores = new HashMap<>();
    for (var org : this.organizationRecommendations) {
      final var score =
          ORGANISATION_PROFILE_MATCH_BOOST_FACTOR * overlapCoefficientCalculator.calculate(seedTags, org.tags());
      addConfidenceScore(this.organizationConfidenceScores, org.id(), score, 1);
    }

    var projectScores = this.getProjectConfidenceScores();
    if(projectScores == null || projectScores.isEmpty()) {
      return;
    }

    for (var entry : projectScores.entrySet()) {
      var project = projectRecommendations.stream().filter(p -> p.id().equals(entry.getKey())).findFirst()
          .orElse(null);
      if (project == null) {
        continue;
      }

      var score = entry.getValue();

      var partner = project.partner();
      if (score > 0.0 && partner != null) {
        addConfidenceScore(this.organizationConfidenceScores, partner, score, 2.0);
      }
    }
  }

  private void calculateProjectConfidenceScores() {
    this.projectConfidenceScores = new HashMap<>();
    for (var project : this.projectRecommendations) {
      final var score =
          PROJECT_MATCH_BOOST_FACTOR * overlapCoefficientCalculator.calculate(seedTags, project.tags());
      addConfidenceScore(this.projectConfidenceScores, project.id(), score, 1);
    }
  }

  private <T> void addConfidenceScore(final Map<T, Double> map, final T obj, final double score,
      final double divisor) {
    map.compute(obj, (key, value) -> {
      if (value == null) {
        return score;
      }
      return (value + score) / divisor;
    });
  }
}
