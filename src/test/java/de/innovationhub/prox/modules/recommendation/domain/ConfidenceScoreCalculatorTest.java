package de.innovationhub.prox.modules.recommendation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ConfidenceScoreCalculatorTest {

  List<UUID> seedTags = List.of(UUID.randomUUID(), UUID.randomUUID());
  List<UUID> mixedTags = List.of(seedTags.get(0), UUID.randomUUID());
  List<UUID> randomTags = List.of(UUID.randomUUID(), UUID.randomUUID());
  List<OrganizationRecommendation> givenOrganizations = List.of(
      new OrganizationRecommendation(UUID.randomUUID(), new HashSet<>(seedTags)),
      new OrganizationRecommendation(UUID.randomUUID(), new HashSet<>(mixedTags)),
      new OrganizationRecommendation(UUID.randomUUID(), new HashSet<>(randomTags))
  );
  UUID supervisorId = UUID.randomUUID();
  List<ProjectRecommendation> givenProjects = List.of(
      new ProjectRecommendation(UUID.randomUUID(), Set.of(supervisorId), UUID.randomUUID(),
          new HashSet<>(seedTags)),
      new ProjectRecommendation(UUID.randomUUID(), Set.of(UUID.randomUUID()), UUID.randomUUID(),
          new HashSet<>(mixedTags)),
      new ProjectRecommendation(UUID.randomUUID(), Set.of(UUID.randomUUID()), UUID.randomUUID(),
          new HashSet<>(randomTags))
  );
  List<LecturerRecommendation> givenLecturers = List.of(
      new LecturerRecommendation(supervisorId, new HashSet<>(seedTags)),
      new LecturerRecommendation(UUID.randomUUID(), new HashSet<>(mixedTags)),
      new LecturerRecommendation(UUID.randomUUID(), new HashSet<>(randomTags))
  );
  ConfidenceScoreCalculator confidenceScoreCalculator = new ConfidenceScoreCalculator(
      givenProjects,
      givenLecturers,
      givenOrganizations,
      seedTags
  );

  @Test
  void shouldAddMissingLecturersFromProjects() {
    assertThat(confidenceScoreCalculator.getLecturerConfidenceScores())
        .hasSize(4);
  }

  @Test
  void shouldAddMissingOrganizationsFromProjects() {
    assertThat(confidenceScoreCalculator.getOrganizationConfidenceScores())
        .hasSize(5);
  }

  @Test
  void shouldGetProjectConfidenceScores() {
    assertThat(confidenceScoreCalculator.getProjectConfidenceScores())
        .hasSize(3)
        .containsEntry(givenProjects.get(0).id(), 1.0)
        .containsEntry(givenProjects.get(1).id(), 0.5)
        .containsEntry(givenProjects.get(2).id(), 0.0);
  }

  @Test
  void shouldGetLecturerConfidenceScores() {
    assertThat(confidenceScoreCalculator.getLecturerConfidenceScores())
        .containsEntry(givenLecturers.get(0).id(), 1.75)
        .containsEntry(givenLecturers.get(1).id(), 1.25)
        .containsEntry(givenLecturers.get(2).id(), 0.0);
  }

  @Test
  void shouldGetOrganizationConfidenceScores() {
    assertThat(confidenceScoreCalculator.getOrganizationConfidenceScores())
        .containsEntry(givenOrganizations.get(0).id(), 2.5)
        .containsEntry(givenOrganizations.get(1).id(), 1.25)
        .containsEntry(givenOrganizations.get(2).id(), 0.0);
  }
}