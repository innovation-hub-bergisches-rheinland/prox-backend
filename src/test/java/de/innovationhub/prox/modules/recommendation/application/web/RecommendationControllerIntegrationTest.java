package de.innovationhub.prox.modules.recommendation.application.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.ClearDatabase;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.project.domain.project.Author;
import de.innovationhub.prox.modules.project.domain.project.CurriculumContext;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
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

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @Test
  void shouldGetRecommendations() {
    // It's a bit hard to test this, we just test that the endpoint is reachable and returns a
    // reasonable result without crashing.
    // Especially it is hard to setup test data. Maybe we need a pre-populated database for this.

    var seedTags = List.of(UUID.randomUUID(), UUID.randomUUID());
    var randomTags = List.of(UUID.randomUUID(), UUID.randomUUID());
    var mixedTags = List.of(seedTags.get(0), randomTags.get(0));

    var lecturer1 = createLecturer(seedTags);
    var lecturer2 = createLecturer(randomTags);
    var lecturer3 = createLecturer(mixedTags);
    userProfileRepository.saveAll(List.of(lecturer1, lecturer2, lecturer3));

    var organization1 = createDummyOrganization(seedTags);
    var organization2 = createDummyOrganization(randomTags);
    var organization3 = createDummyOrganization(mixedTags);
    organizationRepository.saveAll(List.of(organization1, organization2, organization3));

    var project1 = createDummyProject(organization1.getId(), List.of(lecturer1.getId()), seedTags);
    var project2 = createDummyProject(organization2.getId(), List.of(lecturer2.getId()), randomTags);
    var project3 = createDummyProject(organization3.getId(), List.of(lecturer3.getId()), mixedTags);
    projectRepository.saveAll(List.of(project1, project2, project3));


    given()
        .accept(ContentType.JSON)
        .param("seedTags", seedTags)
        .when()
        .get("recommendations")
        .then()
        .log().all()
        .statusCode(200)
        .body("lecturers.size()", is(2))
        .body("lecturers[0].confidenceScore", greaterThanOrEqualTo(2.0f))
        .body("lecturers[0].item.id", is(lecturer1.getId().toString()))
        .body("lecturers[1].confidenceScore", greaterThan(0.0f))
        .body("lecturers[1].item.id", is(lecturer2.getId().toString()))
        .body("organizations.size()", is(2))
        .body("organizations[0].confidenceScore", greaterThanOrEqualTo(1.0f))
        .body("organizations[0].item.id", is(organization1.getId().toString()))
        .body("organizations[1].confidenceScore", greaterThan(0.0f))
        .body("organizations[1].item.id", is(organization2.getId().toString()))
        .body("projects.size()", is(2))
        .body("projects[0].confidenceScore", greaterThanOrEqualTo(1.0f))
        .body("projects[0].item.id", is(project1.getId().toString()))
        .body("projects[1].confidenceScore", greaterThan(0.0f))
        .body("projects[1].item.id", is(project2.getId().toString()));
  }

  private UserProfile createLecturer(Collection<UUID> tags) {
    var profile = UserProfile.create(UUID.randomUUID(), "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), true);
    profile.createLecturerProfile(new LecturerProfileInformation());
    profile.tagProfile(tags);
    return profile;
  }

  private Project createDummyProject(UUID organization, Collection<UUID> supervisors, Collection<UUID> tags) {
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
    project.setTags(tags);
    return project;
  }

  private Organization createDummyOrganization(Collection<UUID> tags) {
    var organization = Organization.create("Test Organization", UUID.randomUUID());
    organization.setTags(tags);
    return organization;
  }
}
