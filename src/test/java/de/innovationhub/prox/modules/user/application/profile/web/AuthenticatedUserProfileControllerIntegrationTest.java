package de.innovationhub.prox.modules.user.application.profile.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto.ContactInformationRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.SetTagsRequestDto;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
class AuthenticatedUserProfileControllerIntegrationTest extends AbstractIntegrationTest {

  private static final String AUTH_USER_ID = "00000000-0000-0000-0000-000000000001";
  UUID authUserId = UUID.fromString(AUTH_USER_ID);

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserProfileRepository userProfileRepository;

  @Autowired
  TagCollectionRepository tagCollectionRepository;

  @Autowired
  TagRepository tagRepository;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @AfterEach
  void resetRestAssured() {
    userProfileRepository.deleteAll();
  }

  @ParameterizedTest
  @CsvSource(value = {"GET:user/profile", "POST:user/profile", "PUT:user/profile",
      "POST:user/profile/avatar", "PUT:user/profile/tags"}, delimiter = ':')
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
  void shouldUpdateUserProfile() {
    var profile = createDummyProfile();
    userProfileRepository.save(profile);

    given().accept(ContentType.JSON).contentType(ContentType.JSON).body(
            new CreateUserProfileRequestDto("Xavier Tester Updated", "Lorem Ipsum Updated",
                new ContactInformationRequestDto("Test Updated", "Test Updated", "Test Updated"), true))
        .when()
        .put("/user/profile")
        .then()
        .statusCode(200);

    var updatedProfile = userProfileRepository.findByUserId(authUserId).get();
    assertThat(updatedProfile)
        .satisfies(p -> {
          assertThat(p.getDisplayName()).isEqualTo("Xavier Tester Updated");
          assertThat(p.getVita()).isEqualTo("Lorem Ipsum Updated");
          assertThat(p.getContactInformation())
              .satisfies(ci -> {
                assertThat(ci.getEmail()).isEqualTo("Test Updated");
                assertThat(ci.getHomepage()).isEqualTo("Test Updated");
                assertThat(ci.getTelephone()).isEqualTo("Test Updated");
              });
        });


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

  @Test
  @WithMockUser(value = AUTH_USER_ID, roles = "professor")
  void shouldSetLecturerTags() {
    var profile = createDummyProfile();
    userProfileRepository.save(profile);

    var tags = List.of(Tag.create("Test1"), Tag.create("Test2"));
    tagRepository.saveAll(tags);
    var tagIds = tags.stream().map(Tag::getId).toList();
    var request = new SetTagsRequestDto(tagIds);

    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .put("/user/profile/tags")
        .then()
        .statusCode(200);

    var savedProfile = userProfileRepository.findByUserId(authUserId).get();
    var tagCollection = tagCollectionRepository.findById(savedProfile.getTagCollectionId()).get();
    assertThat(tagCollection.getTags()).containsExactlyInAnyOrderElementsOf(tags);
  }

  private UserProfile createDummyProfile() {
    return UserProfile.create(authUserId, "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), true);
  }
}