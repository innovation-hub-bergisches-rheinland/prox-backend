package de.innovationhub.prox.modules.user.application.search.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import de.innovationhub.prox.modules.user.domain.search.LecturerSearch;
import de.innovationhub.prox.modules.user.domain.search.OrganizationSearch;
import de.innovationhub.prox.modules.user.domain.search.ProjectSearch;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferences;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferencesRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Transactional
class AuthenticatedSearchPreferencesControllerTest extends AbstractIntegrationTest {
  private static final String AUTH_USER_ID = "00000000-0000-0000-0000-000000000001";
  UUID authUserId = UUID.fromString(AUTH_USER_ID);

  @Autowired
  MockMvc mockMvc;

  @Autowired
  SearchPreferencesRepository searchPreferencesRepository;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @Test
  @WithMockUser(username = AUTH_USER_ID)
  void shouldReturnNotFoundWhenNoSearchPreferencesExist() {
    given()
        .when()
        .get("/user/search")
        .then()
        .statusCode(404);
  }

  @Test
  @WithMockUser(username = AUTH_USER_ID)
  void shouldReturnSearchPreferences() {
    var searchPreferences = SearchPreferences.create(
        authUserId,
        UUID.randomUUID(),
        new ProjectSearch(true, Set.of("A"), Set.of("B")),
        new OrganizationSearch(false),
        new LecturerSearch(true)
    );
    searchPreferencesRepository.save(searchPreferences);

    given()
        .when()
        .get("/user/search")
        .then()
        .statusCode(200)
        .body("userId", is(AUTH_USER_ID))
        .body("tagCollectionId", is(searchPreferences.getTagCollectionId().toString()))
        .body("projectSearch.enabled", is(true))
        .body("projectSearch.moduleTypes", containsInAnyOrder("A"))
        .body("projectSearch.disciplines", containsInAnyOrder("B"))
        .body("organizationSearch.enabled", is(false))
        .body("lecturerSearch.enabled", is(true));
  }
}