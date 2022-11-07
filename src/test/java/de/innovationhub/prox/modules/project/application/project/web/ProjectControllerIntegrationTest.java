package de.innovationhub.prox.modules.project.application.project.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.CurriculumContextDto;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerIntegrationTest {
  @Autowired
  MockMvc mockMvc;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @Test
  @WithMockUser(value = "8307f5bc-38fc-44ac-bab7-3c8ef85c1ec4")
  void shouldCreateProject() {
    var createProjectDto = new CreateProjectDto(
        "Test",
        "Test",
        "Test",
        "Test",
        new CurriculumContextDto(List.of(), List.of()),
        null,
        null,
        List.of()
    );

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(createProjectDto)
        .when()
        .post("projects")
        .then()
        .status(HttpStatus.CREATED);
  }
}