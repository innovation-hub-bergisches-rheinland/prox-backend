package de.innovationhub.prox.modules.project.application.project.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.ProjectIntegrationTest;
import de.innovationhub.prox.modules.project.application.project.dto.ApplyCommitmentDto;
import de.innovationhub.prox.modules.project.application.project.dto.CreateProjectRequest;
import de.innovationhub.prox.modules.project.application.project.dto.CreateProjectRequest.TimeBoxDto;
import de.innovationhub.prox.modules.project.application.project.dto.CurriculumContextRequest;
import de.innovationhub.prox.modules.project.application.project.dto.SetProjectStateRequestDto;
import de.innovationhub.prox.modules.project.application.project.dto.SetProjectTagsRequestDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Transactional
@WithMockUser(value = "8307f5bc-38fc-44ac-bab7-3c8ef85c1ec4")
class ProjectControllerIntegrationTest extends ProjectIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  TagRepository tagRepository;

  @Autowired
  TagCollectionRepository tagCollectionRepository;

  @BeforeEach
  void setUpMockMvc() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @Test
  void shouldGetAll() {
    var all = ProjectFixtures.build_project_list();
    projectRepository.saveAll(all);

    given()
        .when()
        .get("/projects")
        .then()
        .statusCode(200)
        .body("content", hasSize(all.size()));
  }

  @Test
  void shouldGetById() {
    var aProject = ProjectFixtures.build_a_project();
    projectRepository.save(aProject);

    given()
        .when()
        .get("/projects/{id}", aProject.getId())
        .then()
        .statusCode(200)
        .body("id", equalTo(aProject.getId().toString()));
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldCreateProposal() {
    var request = new CreateProjectRequest(
        "A Project",
        "A Description",
        "A Summary",
        "A Requirement",
        new CurriculumContextRequest(
            List.of("BA"),
            List.of("INF")
        ),
        new TimeBoxDto(
            LocalDate.EPOCH,
            LocalDate.EPOCH.plusDays(1)
        ),
        UUID.randomUUID(),
        Set.of()
    );

    var id = given()
        .contentType("application/json")
        .accept("application/json")
        .body(request)
        .when()
        .post("/projects")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getUUID("id");

    var found = projectRepository.findById(id).get();
    assertThat(found.getStatus().getState()).isEqualTo(ProjectState.PROPOSED);
    assertThat(found.getAuthor().getUserId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"));
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldCreateProject() {
    var request = new CreateProjectRequest(
        "A Project",
        "A Description",
        "A Summary",
        "A Requirement",
        new CurriculumContextRequest(
            List.of("BA"),
            List.of("INF")
        ),
        new TimeBoxDto(
            LocalDate.EPOCH,
            LocalDate.now()
        ),
        UUID.randomUUID(),
        Set.of(UUID.fromString("00000000-0000-0000-0000-000000000001"))
    );

    var id = given()
        .contentType("application/json")
        .accept("application/json")
        .body(request)
        .when()
        .post("/projects")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getUUID("id");

    var found = projectRepository.findById(id).get();
    assertThat(found.getStatus().getState()).isEqualTo(ProjectState.OFFERED);
    assertThat(found.getAuthor().getUserId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"));
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001", roles = "professor")
  void shouldUpdateProject() {
    var aProject = ProjectFixtures.build_a_project();
    projectRepository.save(aProject);

    var request = new CreateProjectRequest(
        "A Changed Project",
        "A Description",
        "A Summary",
        "A Requirement",
        new CurriculumContextRequest(
            List.of("BA"),
            List.of("INF")
        ),
        new TimeBoxDto(
            LocalDate.EPOCH,
            LocalDate.now()
        ),
        UUID.randomUUID(),
        Set.of(UUID.fromString("00000000-0000-0000-0000-000000000001"))
    );

    given()
        .contentType("application/json")
        .accept("application/json")
        .body(request)
        .when()
        .put("/projects/{id}" , aProject.getId())
        .then()
        .statusCode(200)
        .body("title", equalTo("A Changed Project"));
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001", roles = { "professor" })
  void shouldApplyCommitment() {
    var aProject = ProjectFixtures.build_a_project();
    projectRepository.save(aProject);
    var supervisorId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    var dto = new ApplyCommitmentDto(supervisorId);

    given()
        .contentType("application/json")
        .accept("application/json")
        .body(dto)
        .when()
        .post("/projects/{id}/commitment" , aProject.getId())
        .then()
        .statusCode(200);

    projectRepository.findById(aProject.getId()).ifPresent(project -> {
      assertThat(project.getSupervisors())
          .hasSize(1)
          .extracting(Supervisor::getLecturerId)
          .containsExactly(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    });
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001", roles = { "professor" })
  void shouldSetState() {
    var aProject = ProjectFixtures.build_a_project();
    aProject.applyCommitment(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    projectRepository.save(aProject);

    given()
        .contentType("application/json")
        .accept("application/json")
        .body(new SetProjectStateRequestDto(ProjectState.RUNNING))
        .when()
        .post("/projects/{id}/status" , aProject.getId())
        .then()
        .statusCode(200)
        .body("status.state", is("RUNNING"));

    projectRepository.findById(aProject.getId()).ifPresent(project -> {
      assertThat(project.getStatus().getState()).isEqualTo(ProjectState.RUNNING);
    });
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldDeleteProject() {
    var aProject = ProjectFixtures.build_a_project();
    projectRepository.save(aProject);

    given()
        .when()
        .delete("/projects/{id}" , aProject.getId())
        .then()
        .statusCode(204);

    assertThat(projectRepository.findById(aProject.getId())).isEmpty();
  }

  @Test
  void shouldFind() {
    var aProject = ProjectFixtures.build_a_project();
    projectRepository.save(aProject);

    given()
        .param("status", aProject.getStatus().getState())
        .param("disciplineKeys", aProject.getCurriculumContext().getDisciplines().get(0))
        .param("moduleTypeKeys", aProject.getCurriculumContext().getModuleTypes().get(0))
        .param("text", aProject.getTitle())
        .when()
        .get("/projects/search/filter")
        .then()
        .log().all()
        .statusCode(200)
        .body("content", hasSize(1));
  }

  @Test
  void shouldReturnCorrectPageSize() {
    var projects = IntStream.range(0, 25)
        .mapToObj(i -> ProjectFixtures.build_a_project())
        .toList();
    projectRepository.saveAll(projects);

    given()
        .param("size", 1)
        .param("page", 0)
        .when()
        .get("/projects/search/filter")
        .then()
        .log().all()
        .statusCode(200)
        .body("totalPages", is(25))
        .body("totalElements", is(25));
  }

  @Test
  void shouldFindByPartner() {
    var aProject = ProjectFixtures.build_a_project();
    projectRepository.save(aProject);

    given()
        .param("partner", aProject.getPartner().getOrganizationId())
        .when()
        .get("/projects/search/findByPartner")
        .then()
        .statusCode(200)
        .body("content", hasSize(1));
  }

  @Test
  void shouldFindBySupervisor() {
    var aProject = ProjectFixtures.build_a_project();
    var supervisorId = UUID.randomUUID();
    aProject.applyCommitment(supervisorId);
    projectRepository.save(aProject);

    given()
        .param("supervisor", supervisorId)
        .when()
        .get("/projects/search/findBySupervisor")
        .then()
        .statusCode(200)
        .body("content", hasSize(1));
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldSetTags() {
    var project = ProjectFixtures.build_a_project();
    projectRepository.save(project);

    var tags = List.of(Tag.create("Test1"), Tag.create("Test2"));
    tagRepository.saveAll(tags);
    var tagIds = tags.stream().map(Tag::getId).toList();
    var setTags = new SetProjectTagsRequestDto(tagIds);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(setTags)
        .when()
        .post("projects/{id}/tags", project.getId())
        .then()
        .status(HttpStatus.OK);


    var updatedProject = projectRepository.findById(project.getId()).get();
    var tagCollection = tagCollectionRepository.findById(updatedProject.getTagCollectionId()).get();
    assertThat(tagCollection.getTags()).containsExactlyInAnyOrderElementsOf(tags);
  }
}