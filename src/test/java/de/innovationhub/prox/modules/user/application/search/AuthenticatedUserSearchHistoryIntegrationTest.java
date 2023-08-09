package de.innovationhub.prox.modules.user.application.search;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.user.domain.search.ProjectSearchEntry;
import de.innovationhub.prox.modules.user.domain.search.SearchHistory;
import de.innovationhub.prox.modules.user.domain.search.SearchHistoryRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import java.util.UUID;
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
class AuthenticatedUserSearchHistoryIntegrationTest extends AbstractIntegrationTest {

  private static final String AUTH_USER_ID = "00000000-0000-0000-0000-000000000001";
  UUID authUserId = UUID.fromString(AUTH_USER_ID);

  @Autowired
  MockMvc mockMvc;

  @Autowired
  SearchHistoryRepository searchHistoryRepository;

  @Autowired
  ModuleTypeRepository moduleTypeRepository;

  @Autowired
  DisciplineRepository disciplineRepository;

  @Autowired
  TagRepository tagRepository;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @ParameterizedTest
  @CsvSource(value = {"GET:user/searchHistory"}, delimiter = ':')
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
  void shouldGetSearchHistory() {
    var history = createDummyHistory();
    searchHistoryRepository.save(history);

    given()
        .accept(ContentType.JSON)
        .when()
        .get("/user/searchHistory")
        .then()
        .statusCode(200)
        .body("userId", is(AUTH_USER_ID))
        .body("projectSearches", hasSize(1));
  }

  @Test
  @WithMockUser(AUTH_USER_ID)
  void shouldReturn404WhenNotFound() {
    given()
        .accept(ContentType.JSON)
        .when()
        .get("/user/searchHistory")
        .then()
        .statusCode(404);
  }

  private SearchHistory createDummyHistory() {
    var history = SearchHistory.create(authUserId);
    var discipline = new Discipline("TE", "Test");
    var module = new ModuleType("TE", "Test", List.of("TE"));
    var tag = Tag.create("test");

    disciplineRepository.save(discipline);
    moduleTypeRepository.save(module);
    tagRepository.save(tag);

    var projectSearch = ProjectSearchEntry.create("test", List.of(tag.getId()), List.of(),
        List.of(discipline.getKey()), List.of(module.getKey()));
    history.addSearch(projectSearch);

    return history;
  }
}