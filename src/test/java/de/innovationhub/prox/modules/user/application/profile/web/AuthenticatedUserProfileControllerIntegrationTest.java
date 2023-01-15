package de.innovationhub.prox.modules.user.application.profile.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.user.application.profile.web.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class AuthenticatedUserProfileControllerIntegrationTest extends AbstractIntegrationTest {
  private static final String AUTH_USER_ID = "00000000-0000-0000-0000-000000000001";
  UUID authUserId = UUID.fromString(AUTH_USER_ID);

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

  @ParameterizedTest
  @CsvSource(value = {"GET:user/profile", "POST:user/profile", "PUT:user/profile", "POST:user/profile/avatar"}, delimiter = ':')
  void shouldReturnUnauthorizedWhenUnauthorized(String method, String path) {
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .when()
        .request(method, path)
        .then()
        .statusCode(401);
  }

  @Test
  @WithMockUser(AUTH_USER_ID)
  void shouldGetUserProfile() {
    var profile = createDummyProfile();
    userProfileRepository.save(profile);

    given()
        .accept(ContentType.JSON)
        .when()
        .get("/user/profile")
        .then()
        .statusCode(200)
        .body("displayName", is(profile.getDisplayName()));
  }

  @Test
  @WithMockUser(AUTH_USER_ID)
  void shouldReturn404WhenNotFound() {
    given()
        .accept(ContentType.JSON)
        .when()
        .get("/user/profile")
        .then()
        .statusCode(404);
  }


  @Test
  @WithMockUser(AUTH_USER_ID)
  void shouldCreateUserProfile() {
    var request = new CreateUserProfileRequestDto("Xavier Tester");

    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/user/profile")
        .then()
        .statusCode(201);

    assertThat(userProfileRepository.findByUserId(authUserId).get().getDisplayName())
        .isEqualTo("Xavier Tester");
  }

  @Test
  @WithMockUser(AUTH_USER_ID)
  void shouldUpdateUserProfile() {
    var profile = createDummyProfile();
    userProfileRepository.save(profile);

    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new CreateUserProfileRequestDto("Xavier Tester Updated"))
        .when()
        .put("/user/profile")
        .then()
        .statusCode(200);

    assertThat(userProfileRepository.findByUserId(authUserId).get().getDisplayName())
        .isEqualTo("Xavier Tester Updated");
  }

  @Test
  @WithMockUser(AUTH_USER_ID)
  void shouldSetAvatar() throws IOException {
    var profile = createDummyProfile();
    userProfileRepository.save(profile);

    var resource = new ClassPathResource("img/avatar.png").getFile();

    given()
        .contentType(ContentType.MULTIPART)
        .multiPart("image", resource, "image/png")
        .when()
        .post("user/profile/avatar")
        .then()
        .status(HttpStatus.NO_CONTENT);

    var updatedProfile = userProfileRepository.findByUserId(authUserId).get();
    assertThat(updatedProfile.getAvatarKey()).isNotNull();
  }

  private UserProfile createDummyProfile() {
    return UserProfile.create(authUserId, "Xavier Tester");
  }
}