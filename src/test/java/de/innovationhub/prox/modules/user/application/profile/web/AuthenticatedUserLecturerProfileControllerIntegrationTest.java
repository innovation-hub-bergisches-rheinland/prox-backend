package de.innovationhub.prox.modules.user.application.profile.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateLecturerRequestDto.CreateLecturerProfileDto;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
class AuthenticatedUserLecturerProfileControllerIntegrationTest extends AbstractIntegrationTest {
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
  @WithMockUser(value = AUTH_USER_ID)
  @CsvSource(value = {"POST:user/profile/lecturer", "PUT:user/profile/lecturer"}, delimiter = ':')
  void shouldReturnForbiddenWhenNotInRole(String method, String path) {
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .when()
        .request(method, path)
        .then()
        .statusCode(403);
  }

  @Test
  @WithMockUser(value = AUTH_USER_ID, roles = "professor")
  void shouldCreateLecturerProfile() {
    var profile = createDummyProfile();
    userProfileRepository.save(profile);
    var request = createLecturerRequestDto();

    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/user/profile/lecturer")
        .then()
        .statusCode(201);

    assertThat(userProfileRepository.findByUserId(authUserId).get().getLecturerProfile())
        .isNotNull();
  }

  @Test
  @WithMockUser(value = AUTH_USER_ID, roles = "professor")
  void shouldUpdateLecturerProfile() {
    var profile = createDummyProfile();
    var lecturerProfile = new LecturerProfileInformation();
    profile.createLecturerProfile(false, lecturerProfile);
    userProfileRepository.save(profile);

    var request = createLecturerRequestDto();

    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .put("/user/profile/lecturer")
        .then()
        .statusCode(200);

    assertThat(userProfileRepository.findByUserId(authUserId).get().getLecturerProfile().getProfile())
        .isNotEqualTo(lecturerProfile);
  }

  private UserProfile createDummyProfile() {
    return UserProfile.create(authUserId, "Xavier Tester", "Lorem Ipsum");
  }

  private CreateLecturerRequestDto createLecturerRequestDto() {
    return new CreateLecturerRequestDto(
        new CreateLecturerProfileDto(
            "affiliation",
            "subject",
            "vita",
            List.of("publication"),
            "room",
            "consultationHour",
            "email",
            "telephone",
            "homepage",
            "collegePage"
        ), false
    );
  }
}