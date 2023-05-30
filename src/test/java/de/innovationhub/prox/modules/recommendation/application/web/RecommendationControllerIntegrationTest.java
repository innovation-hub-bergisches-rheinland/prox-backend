package de.innovationhub.prox.modules.recommendation.application.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.ClearDatabase;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.project.domain.project.Author;
import de.innovationhub.prox.modules.project.domain.project.CurriculumContext;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
//@ClearDatabase
class RecommendationControllerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  MockMvc mockMvc;

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
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @Test
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

    var project1 = createDummyProject(organization1.getId(), List.of(lecturer1.getUserId()), seedTags);
    var project2 = createDummyProject(organization2.getId(), List.of(lecturer2.getUserId()), randomTags);
    var project3 = createDummyProject(organization3.getId(), List.of(lecturer3.getUserId()), mixedTags);
    projectRepository.saveAll(List.of(project1, project2, project3));


    given()
        .accept(ContentType.JSON)
        .param("seedTags", seedTags.stream().map(t -> t.getId().toString()).toList())
        .when()
        .get("recommendations")
        .then()
        .log().all()
        .statusCode(200)
        .body("lecturers.size()", is(2))
        .body("lecturers[0].confidenceScore", equalTo(1.0f))
        .body("lecturers[0].item.userId", notNullValue())
        .body("lecturers[1].confidenceScore", equalTo(1.0f))
        .body("lecturers[1].item.userId", notNullValue())
        .body("organizations.size()", is(2))
        .body("organizations[0].confidenceScore", equalTo(1.0f))
        .body("organizations[0].item.id", notNullValue())
        .body("organizations[1].confidenceScore", greaterThanOrEqualTo(0.875f))
        .body("organizations[1].item.id", notNullValue())
        .body("projects.size()", is(2))
        .body("projects[0].confidenceScore", greaterThanOrEqualTo(1.0f))
        .body("projects[0].item.id", is(project1.getId().toString()))
        .body("projects[1].confidenceScore", greaterThan(0.0f))
        .body("projects[1].item.id", is(project3.getId().toString()));
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

    var project1 = createDummyProject(organization1.getId(), List.of(lecturer1.getUserId()), seedTags);
    var project2 = createDummyProject(organization2.getId(), List.of(lecturer2.getUserId()), seedTags);
    projectRepository.saveAll(List.of(project1, project2));

    var excludedIds = List.of(lecturer2.getUserId().toString(), organization2.getId().toString(), project2.getId().toString());

    given()
        .accept(ContentType.JSON)
        .param("seedTags", seedTags.stream().map(t -> t.getId().toString()).toList())
        .param("excludedIds", excludedIds)
        .when()
        .get("recommendations")
        .then()
        .log().all()
        .statusCode(200)
        .body("lecturers.size()", is(1))
        .body("lecturers[0].item.userId", is(lecturer1.getUserId().toString()))
        .body("organizations.size()", is(1))
        .body("organizations[0].item.id", is(organization1.getId().toString()))
        .body("projects.size()", is(1))
        .body("projects[0].item.id", is(project1.getId().toString()));
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

  private Project createDummyProject(UUID organization, Collection<UUID> supervisors, Collection<Tag> tags) {
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
