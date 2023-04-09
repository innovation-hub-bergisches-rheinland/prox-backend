package de.innovationhub.prox.modules.recommendation.application.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
class RecommendationControllerIntegrationTest extends AbstractIntegrationTest {
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
  void shouldGetRecommendations() {
    given()
        .accept(ContentType.JSON)
        .param("seedTags", List.of(UUID.randomUUID(), UUID.randomUUID()))
        .when()
        .get("recommendations")
        .then()
        .statusCode(200);
  }
}
