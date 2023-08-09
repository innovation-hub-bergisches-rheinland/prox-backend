package de.innovationhub.prox.modules.recommendation.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.project.domain.project.Author;
import de.innovationhub.prox.modules.project.domain.project.CurriculumContext;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import io.github.artsok.RepeatedIfExceptionsTest;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GetRecommendationsHandlerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  GetRecommendationsHandler getRecommendationsHandler;

  @Autowired
  UserProfileRepository userProfileRepository;

  @Autowired
  OrganizationRepository organizationRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  TagRepository tagRepository;

  @Autowired
  TagCollectionRepository tagCollectionRepository;

  @BeforeEach
  void setup() {
    this.userProfileRepository.deleteAll();
    this.organizationRepository.deleteAll();
    this.projectRepository.deleteAll();
    this.tagRepository.deleteAll();
    this.tagCollectionRepository.deleteAll();
  }

  // Flaky Test
  @RepeatedIfExceptionsTest(repeats = 3)
  void shouldGetRecommendations() {
    // It's a bit hard to test this, we just test that the endpoint is reachable and returns a
    // reasonable result without crashing.
    // Especially it is hard to setup test data. Maybe we need a pre-populated database for this.
    var seedTags = List.of(Tag.create("test1"), Tag.create("test2"));
    var randomTags = List.of(Tag.create("test3"), Tag.create("test4"));
    var mixedTags = List.of(seedTags.get(0), randomTags.get(0));

    tagRepository.saveAll(seedTags);
    tagRepository.saveAll(randomTags);

    var lecturer1 = createLecturer(seedTags);
    var lecturer2 = createLecturer(randomTags);
    var lecturer3 = createLecturer(mixedTags);
    userProfileRepository.saveAll(List.of(lecturer1, lecturer2, lecturer3));

    var organization1 = createDummyOrganization(seedTags);
    var organization2 = createDummyOrganization(randomTags);
    var organization3 = createDummyOrganization(mixedTags);
    organizationRepository.saveAll(List.of(organization1, organization2, organization3));

    var project1 = createDummyProject(organization1.getId(), List.of(lecturer1.getUserId()),
        seedTags);
    var project2 = createDummyProject(organization2.getId(), List.of(lecturer2.getUserId()),
        randomTags);
    var project3 = createDummyProject(organization3.getId(), List.of(lecturer3.getUserId()),
        mixedTags);
    projectRepository.saveAll(List.of(project1, project2, project3));

    var recommendations = getRecommendationsHandler.handle(
        new RecommendationRequest(
            seedTags.stream().map(Tag::getId).toList(), List.of(), 5
        )
    );

    assertThat(recommendations.projects())
        .hasSize(2)
        .satisfies(
            projects -> {
              assertThat(projects.get(0).confidenceScore()).isGreaterThan(0.0);
              assertThat(projects.get(0).item().id()).isEqualTo(project1.getId());
              assertThat(projects.get(1).confidenceScore()).isGreaterThan(0.0);
              assertThat(projects.get(1).item().id()).isEqualTo(project3.getId());
            }
        );
    assertThat(recommendations.organizations()).hasSize(2)
        .satisfies(
            organizations -> {
              assertThat(organizations.get(0).confidenceScore()).isGreaterThan(0.0);
              assertThat(organizations.get(0).item().id()).isEqualTo(organization1.getId());
              assertThat(organizations.get(1).confidenceScore()).isGreaterThan(0.0);
              assertThat(organizations.get(1).item().id()).isEqualTo(organization3.getId());
            }
        );
    assertThat(recommendations.lecturers()).hasSize(2)
        .satisfies(
            lecturers -> {
              assertThat(lecturers.get(0).confidenceScore()).isGreaterThan(0.0);
              assertThat(lecturers.get(0).item().userId()).isEqualTo(lecturer1.getUserId());
              assertThat(lecturers.get(1).confidenceScore()).isGreaterThan(0.0);
              assertThat(lecturers.get(1).item().userId()).isEqualTo(lecturer3.getUserId());
            }
        );
  }

  @Test
  void shouldExcludeRecommendations() {
    var seedTags = List.of(Tag.create("test1"), Tag.create("test2"));
    tagRepository.saveAll(seedTags);

    var lecturer1 = createLecturer(seedTags);
    var lecturer2 = createLecturer(seedTags);
    userProfileRepository.saveAll(List.of(lecturer1, lecturer2));

    var organization1 = createDummyOrganization(seedTags);
    var organization2 = createDummyOrganization(seedTags);
    organizationRepository.saveAll(List.of(organization1, organization2));

    var project1 = createDummyProject(organization1.getId(), List.of(lecturer1.getUserId()),
        seedTags);
    var project2 = createDummyProject(organization2.getId(), List.of(lecturer2.getUserId()),
        seedTags);
    projectRepository.saveAll(List.of(project1, project2));

    var recommendations = getRecommendationsHandler.handle(
        new RecommendationRequest(
            seedTags.stream().map(Tag::getId).toList(), List.of(
            project1.getId(), organization1.getId(), lecturer1.getUserId()
        ), 5
        )
    );

    assertThat(recommendations.projects())
        .hasSize(1)
        .satisfies(
            projects -> {
              assertThat(projects.get(0).confidenceScore()).isGreaterThan(0.0);
              assertThat(projects.get(0).item().id()).isEqualTo(project2.getId());
            }
        );
    assertThat(recommendations.organizations()).hasSize(1)
        .satisfies(
            organizations -> {
              assertThat(organizations.get(0).confidenceScore()).isGreaterThan(0.0);
              assertThat(organizations.get(0).item().id()).isEqualTo(organization2.getId());
            }
        );
    assertThat(recommendations.lecturers()).hasSize(1)
        .satisfies(
            lecturers -> {
              assertThat(lecturers.get(0).confidenceScore()).isGreaterThan(0.0);
              assertThat(lecturers.get(0).item().userId()).isEqualTo(lecturer2.getUserId());
            }
        );
  }

  private UserProfile createLecturer(Collection<Tag> tags) {
    var profile = UserProfile.create(UUID.randomUUID(), "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), true);
    profile.createLecturerProfile(new LecturerProfileInformation());

    var tagCollection = TagCollection.create(profile.getUserId());
    tagCollection.setTags(tags);
    tagCollectionRepository.save(tagCollection);
    profile.setTagCollectionId(tagCollection.getId());

    return profile;
  }

  private Project createDummyProject(UUID organization, Collection<UUID> supervisors,
      Collection<Tag> tags) {
    var project = Project.create(
        new Author(UUID.randomUUID()),
        "Test Project",
        "Test Project",
        "Test Project",
        "Test Project",
        CurriculumContext.EMPTY,
        null,
        organization,
        supervisors
    );

    var tagCollection = TagCollection.create(project.getId());
    tagCollection.setTags(tags);
    tagCollectionRepository.save(tagCollection);

    project.setTagCollection(tagCollection.getId());

    return project;
  }

  private Organization createDummyOrganization(Collection<Tag> tags) {
    var organization = Organization.create("Test Organization", UUID.randomUUID());

    var tagCollection = TagCollection.create(organization.getId());
    tagCollection.setTags(tags);
    tagCollectionRepository.save(tagCollection);

    organization.setTagCollectionId(tagCollection.getId());

    return organization;
  }
}