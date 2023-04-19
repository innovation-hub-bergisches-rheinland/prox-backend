package de.innovationhub.prox.modules.user.application.profile.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class LecturerProfileControllerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserProfileRepository userProfileRepository;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }


  @Test
  void shouldListLecturers() {
    var lecturer = createDummyLecturer(true);
    userProfileRepository.save(lecturer);

    given()
        .accept(ContentType.JSON)
        .when()
        .get("lecturers")
        .then()
        .statusCode(200)
        .body("content.size()", is(1))
        .body("content[0].userId", is(lecturer.getUserId().toString()));
  }

  @Test
  void shouldNotListInvisibleLecturers() {
    var lecturer = createDummyLecturer(false);
    userProfileRepository.save(lecturer);

    given()
        .accept(ContentType.JSON)
        .when()
        .get("lecturers")
        .then()
        .statusCode(200)
        .body("content.size()", is(0));
  }

  @Test
  void shouldSearch() {
    var lecturer = createDummyLecturer(true);
    userProfileRepository.save(lecturer);
    var lecturerThatDoesntSatisfyQuery = createDummyLecturer(true);
    lecturerThatDoesntSatisfyQuery.update("Some other name", "Lorem Ipsum",
        new ContactInformation("", "", ""), true);
    userProfileRepository.save(lecturerThatDoesntSatisfyQuery);


    given()
        .accept(ContentType.JSON)
        .param("q", lecturer.getDisplayName())
        .when()
        .get("lecturers/search/filter")
        .then()
        .statusCode(200)
        .body("content.size()", is(1))
        .body("content[0].userId", is(lecturer.getUserId().toString()));
  }

  @Test
  void shouldNotSearchInvisible() {
    var lecturer = createDummyLecturer(false);
    userProfileRepository.save(lecturer);

    given()
        .accept(ContentType.JSON)
        .param("q", lecturer.getDisplayName())
        .when()
        .get("lecturers/search/filter")
        .then()
        .statusCode(200)
        .body("content.size()", is(0));
  }

  private UserProfile createDummyLecturer(boolean visible) {
    var up = UserProfile.create(UUID.randomUUID(), "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), visible);
    up.createLecturerProfile(new LecturerProfileInformation());
    return up;
  }
}