package de.innovationhub.prox.modules.project.application.project.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import de.innovationhub.prox.AbstractIntegrationTest;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
//@ClearDatabase
class ProjectRecommendationIntegrationTest extends AbstractIntegrationTest {

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
    var seedTags = List.of(Tag.create("test1"), Tag.create("test2"));

    tagRepository.saveAll(seedTags);

    var lecturer1 = createLecturer(seedTags);
    userProfileRepository.saveAll(List.of(lecturer1));

    var organization1 = createDummyOrganization(seedTags);
    organizationRepository.saveAll(List.of(organization1));

    var project1 = createDummyProject(organization1.getId(), List.of(lecturer1.getUserId()),
        seedTags);
    projectRepository.saveAll(List.of(project1));

    given()
        .accept(ContentType.JSON)
        .when()
        .get("/projects/{id}/recommendations", project1.getId().toString())
        .then()
        .log().all()
        .statusCode(200);
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
