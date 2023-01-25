package de.innovationhub.prox.modules.user.application.profile.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
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
class UserProfileControllerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserProfileRepository userProfileRepository;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @AfterEach
  void resetRestAssured() {
    userProfileRepository.deleteAll();
  }


  @Test
  void shouldFindUser() {
    var profile = createDummyProfile();
    userProfileRepository.save(profile);

    given()
        .accept(ContentType.JSON)
        .when()
        .get("users/{id}", profile.getUserId())
        .then()
        .statusCode(200)
        .body("userId", is(profile.getUserId().toString()));
  }

  @Test
  void shouldReturnNotFoundWhenNotFound() {
    given()
        .accept(ContentType.JSON)
        .when()
        .get("users/{id}", UUID.randomUUID())
        .then()
        .statusCode(404);
  }

  private UserProfile createDummyProfile() {
    var up = UserProfile.create(UUID.randomUUID(), "Xavier Tester", "Lorem Ipsum", new ContactInformation("Test", "Test", "Test"));
    return up;
  }
}