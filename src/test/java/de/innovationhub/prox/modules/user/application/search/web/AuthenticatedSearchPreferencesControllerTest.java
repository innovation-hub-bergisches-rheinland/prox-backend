package de.innovationhub.prox.modules.user.application.search.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.user.application.search.dto.CreateSearchPreferencesRequest;
import de.innovationhub.prox.modules.user.domain.search.LecturerSearch;
import de.innovationhub.prox.modules.user.domain.search.OrganizationSearch;
import de.innovationhub.prox.modules.user.domain.search.ProjectSearch;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferences;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferencesRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.UUID;
import org.instancio.Instancio;
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

  @Test
  @WithMockUser(username = AUTH_USER_ID)
  void shouldCreateSearchPreferences() {
    var searchPreferencesDto = Instancio.of(CreateSearchPreferencesRequest.class).create();

    given()
        .body(searchPreferencesDto)
        .contentType("application/json")
        .accept("application/json")
        .when()
        .post("/user/search")
        .then()
        .statusCode(201)
        .body("userId", is(AUTH_USER_ID));

    var searchPreferences = searchPreferencesRepository.findByUserId(authUserId).orElseThrow();
    assertThat(searchPreferences).satisfies(sp -> {
      assertThat(sp.getUserId()).isEqualTo(authUserId);
      assertThat(sp.getTagCollectionId()).isEqualTo(searchPreferencesDto.tagCollectionId());
      assertThat(sp.getProjectSearch().getEnabled()).isEqualTo(searchPreferences.getProjectSearch().getEnabled());
      assertThat(sp.getProjectSearch().getDisciplines()).containsExactlyInAnyOrderElementsOf(searchPreferences.getProjectSearch().getDisciplines());
      assertThat(sp.getProjectSearch().getModuleTypes()).containsExactlyInAnyOrderElementsOf(searchPreferences.getProjectSearch().getModuleTypes());
      assertThat(sp.getOrganizationSearch().getEnabled()).isEqualTo(searchPreferences.getOrganizationSearch().getEnabled());
      assertThat(sp.getLecturerSearch().getEnabled()).isEqualTo(searchPreferences.getLecturerSearch().getEnabled());
    });
  }

  @Test
  @WithMockUser(username = AUTH_USER_ID)
  void shouldUpdateSearchPreferences() {
    var searchPreferences = SearchPreferences.create(
        authUserId,
        UUID.randomUUID(),
        new ProjectSearch(true, Set.of("A"), Set.of("B")),
        new OrganizationSearch(false),
        new LecturerSearch(true)
    );
    searchPreferencesRepository.save(searchPreferences);

    var searchPreferencesDto = Instancio.of(CreateSearchPreferencesRequest.class).create();

    given()
        .body(searchPreferencesDto)
        .contentType("application/json")
        .accept("application/json")
        .when()
        .put("/user/search")
        .then()
        .statusCode(200)
        .body("userId", is(AUTH_USER_ID));

    var foundSearchPreferences = searchPreferencesRepository.findByUserId(authUserId).orElseThrow();
    assertThat(foundSearchPreferences).satisfies(sp -> {
      assertThat(sp.getUserId()).isEqualTo(authUserId);
      assertThat(sp.getTagCollectionId()).isEqualTo(searchPreferencesDto.tagCollectionId());
      assertThat(sp.getProjectSearch().getEnabled()).isEqualTo(foundSearchPreferences.getProjectSearch().getEnabled());
      assertThat(sp.getProjectSearch().getDisciplines()).containsExactlyInAnyOrderElementsOf(foundSearchPreferences.getProjectSearch().getDisciplines());
      assertThat(sp.getProjectSearch().getModuleTypes()).containsExactlyInAnyOrderElementsOf(foundSearchPreferences.getProjectSearch().getModuleTypes());
      assertThat(sp.getOrganizationSearch().getEnabled()).isEqualTo(foundSearchPreferences.getOrganizationSearch().getEnabled());
      assertThat(sp.getLecturerSearch().getEnabled()).isEqualTo(foundSearchPreferences.getLecturerSearch().getEnabled());
    });
  }
}