package de.innovationhub.prox.modules.user.application.star.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.project.application.project.event.StarIntegrationEventListeners;
import de.innovationhub.prox.modules.user.domain.star.StarCollection;
import de.innovationhub.prox.modules.user.domain.star.StarCollectionRepository;
import de.innovationhub.prox.modules.user.domain.user.ProxUser;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Transactional
class AuthenticatedUserStarControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  StarCollectionRepository starCollectionRepository;

  @Autowired
  MockMvc mockMvc;

  // TODO: This is a workaround as event listeners are invoked synchronous and it will fail because
  //  project is not existent.
  //  Either create the project or use async events using rabbitmq
  @MockBean
  StarIntegrationEventListeners starIntegrationEventListeners;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  private ProxUser createDummyUser(UUID id) {
    return new ProxUser(id, "Xavier Tester", "xavier.tester@example.com");
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldStarProject() {
    var userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    var projectId = UUID.randomUUID();
    var collection = new StarCollection(userId);
    starCollectionRepository.save(collection);

    given()
        .accept(ContentType.JSON)
        .when()
        .put("user/stars/projects/{id}", projectId)
        .then()
        .status(HttpStatus.NO_CONTENT);

    var updatedCollection = starCollectionRepository.findByUserId(userId).get();
    assertThat(updatedCollection.getStarredProjects())
        .contains(projectId);
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldUnstarProject() {
    var userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    var projectId = UUID.randomUUID();
    var collection = new StarCollection(userId);
    collection.starProject(projectId);

    starCollectionRepository.save(collection);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete("user/stars/projects/{id}", projectId)
        .then()
        .status(HttpStatus.NO_CONTENT);

    var updatedCollection = starCollectionRepository.findByUserId(userId).get();
    assertThat(updatedCollection.getStarredProjects())
        .doesNotContain(projectId);
  }
}