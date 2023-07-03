package de.innovationhub.prox.modules.recommendation.domain;

import de.innovationhub.prox.modules.recommendation.domain.calc.OverlapCoefficientCalculator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

  protected ConfidenceScoreCalculator(
      List<ProjectRecommendation> projectRecommendations,
      List<LecturerRecommendation> lecturerRecommendations,
      List<OrganizationRecommendation> organizationRecommendations,
      List<UUID> seedTags
  ) {
    this.projectRecommendations = projectRecommendations;
    this.lecturerRecommendations = lecturerRecommendations;
    this.organizationRecommendations = organizationRecommendations;
    this.seedTags = seedTags;
  }

  public static ConfidenceScoreCalculatorBuilder builder() {
    return new ConfidenceScoreCalculatorBuilder();
  }

  public Map<UUID, Double> getLecturerConfidenceScores() {
    if (lecturerConfidenceScores == null) {
      this.lecturerConfidenceScores = calculateLecturerConfidenceScores();
    }
    return Map.copyOf(lecturerConfidenceScores);
  }

  public Map<UUID, Double> getOrganizationConfidenceScores() {
    if (organizationConfidenceScores == null) {
      this.organizationConfidenceScores = calculateOrganizationConfidenceScores();
    }
    return Map.copyOf(organizationConfidenceScores);
  }

  public Map<UUID, Double> getProjectConfidenceScores() {
    if (projectConfidenceScores == null) {
      this.projectConfidenceScores = calculateProjectConfidenceScores();
    }
    return Map.copyOf(projectConfidenceScores);
  }

  private Map<UUID, Double> calculateLecturerConfidenceScores() {
    var results = new HashMap<UUID, Double>();
    for (var supervisor : this.lecturerRecommendations) {
      final var score =
          LECTURER_PROFILE_MATCH_BOOST_FACTOR * overlapCoefficientCalculator.calculate(seedTags,
              supervisor.tags());
      addConfidenceScore(results, supervisor.id(), score, 1);
    }

    var projectScores = this.getProjectConfidenceScores();
    if (projectScores == null || projectScores.isEmpty()) {
      return results;
    }

    for (var entry : projectScores.entrySet()) {
      var project = projectRecommendations.stream().filter(p -> p.id().equals(entry.getKey()))
          .findFirst()
          .orElse(null);
      if (project == null) {
        continue;
      }

      var score = entry.getValue();
      var supervisors = project.supervisors();
      if (score > 0.0 && supervisors != null) {
        for (var supervisor : supervisors) {
          addConfidenceScore(results, supervisor, score, 2.0);
        }
      }
    }

    return results;
  }

  private Map<UUID, Double> calculateOrganizationConfidenceScores() {
    var results = new HashMap<UUID, Double>();
    for (var org : this.organizationRecommendations) {
      final var score =
          ORGANISATION_PROFILE_MATCH_BOOST_FACTOR * overlapCoefficientCalculator.calculate(seedTags,
              org.tags());
      addConfidenceScore(results, org.id(), score, 1);
    }

    var projectScores = this.getProjectConfidenceScores();
    if (projectScores == null || projectScores.isEmpty()) {
      return results;
    }

    for (var entry : projectScores.entrySet()) {
      var project = projectRecommendations.stream().filter(p -> p.id().equals(entry.getKey()))
          .findFirst()
          .orElse(null);
      if (project == null) {
        continue;
      }

      var score = entry.getValue();

      var partner = project.partner();
      if (score > 0.0 && partner != null) {
        addConfidenceScore(results, partner, score, 2.0);
      }
    }
    return results;
  }

  private Map<UUID, Double> calculateProjectConfidenceScores() {
    var results = new HashMap<UUID, Double>();
    for (var project : this.projectRecommendations) {
      final var score =
          PROJECT_MATCH_BOOST_FACTOR * overlapCoefficientCalculator.calculate(seedTags,
              project.tags());
      addConfidenceScore(results, project.id(), score, 1);
    }
    return results;
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

  public static class ConfidenceScoreCalculatorBuilder {

    public List<LecturerRecommendation> lecturerRecommendations = List.of();
    public List<OrganizationRecommendation> organizationRecommendations = List.of();
    public List<ProjectRecommendation> projectRecommendations = List.of();
    public List<UUID> seedTags = List.of();

    protected ConfidenceScoreCalculatorBuilder() {
    }

    public ConfidenceScoreCalculator build() {
      return new ConfidenceScoreCalculator(
          projectRecommendations, lecturerRecommendations, organizationRecommendations, seedTags
      );
    }

    public ConfidenceScoreCalculatorBuilder withSeedLecturers(
        List<LecturerRecommendation> lecturers) {
      Objects.requireNonNull(lecturers);
      this.lecturerRecommendations = lecturers;
      return this;
    }

    public ConfidenceScoreCalculatorBuilder withSeedOrganizations(
        List<OrganizationRecommendation> organizations) {
      Objects.requireNonNull(organizations);
      this.organizationRecommendations = organizations;
      return this;
    }

    public ConfidenceScoreCalculatorBuilder withSeedProjects(List<ProjectRecommendation> projects) {
      Objects.requireNonNull(projects);
      this.projectRecommendations = projects;
      return this;
    }

    public ConfidenceScoreCalculatorBuilder withSeedTags(List<UUID> tags) {
      Objects.requireNonNull(tags);
      this.seedTags = tags;
      return this;
    }
  }
}
