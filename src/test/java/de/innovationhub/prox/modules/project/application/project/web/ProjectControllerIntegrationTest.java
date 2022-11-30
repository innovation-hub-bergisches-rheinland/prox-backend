package de.innovationhub.prox.modules.project.application.project.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.project.DisciplineFixtures;
import de.innovationhub.prox.modules.project.ModuleTypeFixtures;
import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.CurriculumContextDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.PartnerDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SetProjectTagsRequestDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SupervisorDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.TimeBoxDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.UpdateProjectDto;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Transactional
class ProjectControllerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  ModuleTypeRepository moduleTypeRepository;

  @Autowired
  DisciplineRepository disciplineRepository;

  @BeforeEach
  void setUp() {
    disciplineRepository.saveAll(DisciplineFixtures.ALL);
    moduleTypeRepository.saveAll(ModuleTypeFixtures.ALL);
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @AfterEach
  void tearDown() {
    projectRepository.deleteAll();
    moduleTypeRepository.deleteAll();
    disciplineRepository.deleteAll();
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
        .body("projects", hasSize(all.size()));
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
    var request = new CreateProjectDto(
        "A Project",
        "A Description",
        "A Summary",
        "A Requirement",
        new CurriculumContextDto(
            List.of("BA"),
            List.of("INF")
        ),
        new PartnerDto(UUID.randomUUID()),
        new TimeBoxDto(
            LocalDate.EPOCH,
            LocalDate.EPOCH.plusDays(1)
        ),
        List.of()
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
    var request = new CreateProjectDto(
        "A Project",
        "A Description",
        "A Summary",
        "A Requirement",
        new CurriculumContextDto(
            List.of("BA"),
            List.of("INF")
        ),
        new PartnerDto(UUID.randomUUID()),
        new TimeBoxDto(
            LocalDate.EPOCH,
            LocalDate.now()
        ),
        List.of(new SupervisorDto(UUID.randomUUID()))
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
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldUpdateProject() {
    var aProject = ProjectFixtures.build_a_project();
    projectRepository.save(aProject);

    var request = new UpdateProjectDto(
        "A Changed Project",
        "A Description",
        "A Summary",
        "A Requirement",
        new CurriculumContextDto(
            List.of("BA"),
            List.of("INF")
        ),
        new PartnerDto(UUID.randomUUID()),
        new TimeBoxDto(
            LocalDate.EPOCH,
            LocalDate.now()
        ),
        List.of(new SupervisorDto(UUID.randomUUID()))
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

    given()
        .contentType("application/json")
        .accept("application/json")
        .when()
        .post("/projects/{id}/commitment" , aProject.getId())
        .then()
        .statusCode(200)
        .body("supervisors[0].id", is("00000000-0000-0000-0000-000000000001"));

    projectRepository.findById(aProject.getId()).ifPresent(project -> {
      assertThat(project.getSupervisors())
          .hasSize(1)
          .extracting(Supervisor::getLecturerId)
          .containsExactly(UUID.fromString("00000000-0000-0000-0000-000000000001"));
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
        .param("disciplineKeys", aProject.getCurriculumContext().getDisciplines().get(0).getKey())
        .param("moduleTypeKeys", aProject.getCurriculumContext().getModuleTypes().get(0).getKey())
        .param("text", aProject.getTitle())
        .when()
        .get("/projects/search/filter")
        .then()
        .statusCode(200)
        .body("projects", hasSize(1));
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldSetTags() {
    var project = ProjectFixtures.build_a_project();
    projectRepository.save(project);

    var tags = List.of(UUID.randomUUID(), UUID.randomUUID());
    var setTags = new SetProjectTagsRequestDto(tags);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(setTags)
        .when()
        .post("projects/{id}/tags", project.getId())
        .then()
        .status(HttpStatus.OK);

    var updatedProject = projectRepository.findById(project.getId()).get();
    assertThat(updatedProject.getTags()).containsExactlyInAnyOrderElementsOf(tags);
  }
}