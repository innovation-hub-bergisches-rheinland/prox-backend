package de.innovationhub.prox.modules.profile.application.lecturer.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerDto.CreateLecturerProfileDto;
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

@AutoConfigureMockMvc
class LecturerControllerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  MockMvc mockMvc;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @Test
  @WithMockUser(value = "8307f5bc-38fc-44ac-bab7-3c8ef85c1ec4")
  void shouldCreateLecturer() {
    var createLecturerRequest = new CreateLecturerDto(
        "Max Mustermann",
        new CreateLecturerProfileDto(
            "2022-11-07",
            "200",
            "Lorem Ipsum",
            List.of("Lorem Ipsum"),
            "Lorem Ipsum",
            "Lala Land",
            "test@example.org",
            "555-1234-567",
            "example.org",
            "example"
        )
    );

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(createLecturerRequest)
        .when()
        .post("lecturers")
        .then()
        .status(HttpStatus.CREATED);
  }
}