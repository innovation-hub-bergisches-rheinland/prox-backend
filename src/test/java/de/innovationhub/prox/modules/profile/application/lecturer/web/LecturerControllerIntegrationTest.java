package de.innovationhub.prox.modules.profile.application.lecturer.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto.CreateLecturerProfileDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.SetLecturerTagsRequestDto;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Transactional
class LecturerControllerIntegrationTest extends AbstractIntegrationTest {

  static final String USER_ID = "8307f5bc-38fc-44ac-bab7-3c8ef85c1ec4";

  @Autowired
  MockMvc mockMvc;

  @Autowired
  LecturerRepository lecturerRepository;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
    lecturerRepository.deleteAll();
  }

  @AfterEach
  void tearDown() {
    lecturerRepository.deleteAll();
  }

  private Lecturer createDummyLecturer() {
    var lecturer = Lecturer.create(UUID.fromString(USER_ID), "Max Mustermann");
    lecturer.setVisibleInPublicSearch(true);
    return lecturer;
  }

  @Test
  void shouldGetAll() {
    var lecturer = createDummyLecturer();
    lecturerRepository.save(lecturer);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .get("lecturers")
        .then()
        .status(HttpStatus.OK)
        .body("content", hasSize(1));
  }

  @Test
  void shouldReturnNotFound() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .get("lecturers/{id}", UUID.randomUUID())
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldGetOne() {
    var lecturer = createDummyLecturer();
    lecturerRepository.save(lecturer);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .get("lecturers/{id}", lecturer.getId())
        .then()
        .status(HttpStatus.OK)
        .body("id", equalTo(lecturer.getId().toString()));
  }

  @Test
  void shouldFindByName() {
    var lecturer = createDummyLecturer();
    lecturerRepository.save(lecturer);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .param("q", lecturer.getName())
        .when()
        .get("lecturers/search/findByName")
        .then()
        .status(HttpStatus.OK)
        .body("content", hasSize(1));
  }

  @Test
  @WithMockUser(value = USER_ID)
  void shouldUpdate() {
    var lecturer = createDummyLecturer();
    lecturerRepository.save(lecturer);

    var updateLecturerDto = new CreateLecturerRequestDto(
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
        ), false
    );

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(updateLecturerDto)
        .when()
        .put("lecturers/{id}", lecturer.getId().toString())
        .then()
        .status(HttpStatus.OK);

    var updatedLecturer = lecturerRepository.findById(lecturer.getId()).orElseThrow();
    assertThat(updatedLecturer.getName()).isEqualTo("Max Mustermann");
    assertThat(updatedLecturer.getProfile().getAffiliation()).isEqualTo("2022-11-07");
    assertThat(updatedLecturer.getVisibleInPublicSearch()).isFalse();
  }

  @Test
  @WithMockUser(value = USER_ID)
  void shouldSetAvatar() throws IOException {
    var lecturer = createDummyLecturer();
    lecturerRepository.save(lecturer);

    var resource = new ClassPathResource("img/avatar.png").getFile();

    given()
        .contentType(ContentType.MULTIPART)
        .multiPart("image", resource, "image/png")
        .when()
        .post("lecturers/" + lecturer.getId() + "/avatar")
        .then()
        .status(HttpStatus.NO_CONTENT);

    var lecturer1 = lecturerRepository.findById(lecturer.getId()).get();
    assertThat(lecturer1.getAvatarKey()).isNotNull();
  }

  @Test
  @WithMockUser(value = USER_ID)
  void shouldSetTags() {
    var lecturer = createDummyLecturer();
    lecturerRepository.save(lecturer);

    var tags = List.of(UUID.randomUUID(), UUID.randomUUID());
    var setTags = new SetLecturerTagsRequestDto(tags);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(setTags)
        .when()
        .post("lecturers/{id}/tags", lecturer.getId().toString())
        .then()
        .status(HttpStatus.OK);

    var updatedLecturer = lecturerRepository.findById(lecturer.getId()).orElseThrow();
    assertThat(updatedLecturer.getTags()).containsExactlyInAnyOrderElementsOf(tags);
  }

  @Test
  void shouldSearch() {
    var lecturer = createDummyLecturer();
    lecturerRepository.save(lecturer);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .param("q", lecturer.getName())
        .when()
        .get("lecturers/search/filter")
        .then()
        .status(HttpStatus.OK)
        .body("content", hasSize(1));
  }
}