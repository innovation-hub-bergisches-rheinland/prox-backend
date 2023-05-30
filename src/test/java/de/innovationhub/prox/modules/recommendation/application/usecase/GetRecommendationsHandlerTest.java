package de.innovationhub.prox.modules.recommendation.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto.ReadSupervisorDto;
import de.innovationhub.prox.modules.project.domain.project.Author;
import de.innovationhub.prox.modules.project.domain.project.CurriculumContext;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse.RecommendationResult;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;
import org.instancio.Instancio;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class GetRecommendationsHandlerTest {
  TagCollectionFacade tagCollectionFacade = mock(TagCollectionFacade.class);
  UserProfileFacade userProfileFacade = mock(UserProfileFacade.class);
  OrganizationFacade organizationFacade = mock(OrganizationFacade.class);
  ProjectFacade projectFacade = mock(ProjectFacade.class);
  GetRecommendationsHandler getRecommendationsHandler = new GetRecommendationsHandler(tagCollectionFacade, userProfileFacade, organizationFacade, projectFacade);

  @Test
  void shouldNotHaveHigherScoreThanOne() {
    var seedTags = Instancio.ofList(TagDto.class).size(2).create();
    var lecturer = Instancio.of(UserProfileDto.class)
        .supply(field(UserProfileDto::tags), () -> seedTags)
        .create();

    // Let's create 100 Projects that are supervised by the lecturer
    var projects = Instancio.ofList(ProjectDto.class)
        .size(100)
        .supply(field(ProjectDto::supervisors), () -> List.of(new ReadSupervisorDto(lecturer.userId(), lecturer.displayName())))
        .supply(field(ProjectDto::tags), () -> seedTags)
        .create();

    var tagCollections = Stream.concat(
        Stream.of(lecturer.userId()),
        projects.stream().map(ProjectDto::id)
    ).map(id -> new TagCollectionDto(id, seedTags)).toList();

    when(tagCollectionFacade.findWithAnyTag(anyList())).thenReturn(tagCollections);
    when(userProfileFacade.findLecturersByIds(anyList())).thenReturn(List.of(lecturer));
    when(userProfileFacade.getByUserId(lecturer.userId())).thenReturn(Optional.of(lecturer));
    when(projectFacade.findAllByIds(anyList())).thenReturn(projects);
    when(projectFacade.get(any())).thenAnswer(invocation -> {
      var id = invocation.getArgument(0);
      return projects.stream().filter(p -> p.id().equals(id)).findFirst();
    });
    when(userProfileFacade.findLecturersByIds(argThat(ids -> ids.contains(lecturer.userId())))).thenReturn(List.of(lecturer));

    var tagIds = seedTags.stream().map(TagDto::id).toList();
    assertThat(getRecommendationsHandler.handle(new RecommendationRequest(tagIds, List.of(), 3)))
        .satisfies(r -> {
          assertThat(r.lecturers())
              .hasSizeGreaterThanOrEqualTo(1)
              .extracting(RecommendationResult::confidenceScore)
              .allSatisfy(score -> {
                assertThat(score).isPositive().isLessThanOrEqualTo(1);
              });
          assertThat(r.projects())
              .extracting(RecommendationResult::confidenceScore)
              .allSatisfy(score -> {
                assertThat(score).isPositive().isLessThanOrEqualTo(1);
              });
          assertThat(r.organizations())
              .extracting(RecommendationResult::confidenceScore)
              .allSatisfy(score -> {
                assertThat(score).isPositive().isLessThanOrEqualTo(1);
              });
        });
  }

  @Test
  void shouldSortProjectsByDate() {
    var seedTags = Instancio.ofList(TagDto.class).size(2).create();
    var projects = Instancio.ofList(ProjectDto.class)
        .size(100)
        .supply(field(ProjectDto::tags), () -> seedTags) // All projects should have the same score
        .create();
    // Shuffle the projects to ensure that they are not sorted by date (although Instancio does that)
    Collections.shuffle(projects);
    when(projectFacade.findAllByIds(anyList())).thenReturn(projects);
    when(projectFacade.get(any())).thenAnswer(invocation -> {
      var id = invocation.getArgument(0);
      return projects.stream().filter(p -> p.id().equals(id)).findFirst();
    });

    var tagIds = seedTags.stream().map(TagDto::id).toList();
    assertThat(getRecommendationsHandler.handle(new RecommendationRequest(tagIds, List.of(), 10)))
        .satisfies(r -> {
          assertThat(r.projects())
              .isNotEmpty()
              .isSortedAccordingTo((p1, p2) -> p2.item().createdAt().compareTo(p1.item().createdAt()));
        });
  }
}