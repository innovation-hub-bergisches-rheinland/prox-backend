package de.innovationhub.prox.modules.tag.application.tag.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.application.tag.dto.MergeTagsRequest;
import de.innovationhub.prox.modules.tag.application.tag.dto.UpdateTagRequest;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class AdminTagControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  TagCollectionRepository tagCollectionRepository;

  @Autowired
  TagRepository tagRepository;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @ParameterizedTest
  @CsvSource({
      "PUT, /tags/{id}/aliases",
      "POST, /tags/{id}/merge"
  })
  @WithMockUser
  void shouldReturnUnauthorized(String method, String path) {
    path = path.replace("{id}", UUID.randomUUID().toString());
    RestAssuredMockMvc.given()
        .when()
        .request(method, path)
        .then()
        .statusCode(403);
  }

  @Test
  @WithMockUser(roles = "admin")
  void shouldMergeTags() {
    var tags = createTags("tag1", "tag2");
    var tagToMerge = tags.get(0);
    var tagToMergeInto = tags.get(1);

    RestAssuredMockMvc.given()
        .body(new MergeTagsRequest(tagToMergeInto.getId()))
        .accept("application/json")
        .contentType("application/json")
        .when()
        .post("/tags/{id}/merge", tagToMerge.getId())
        .then()
        .statusCode(200)
        .body("id", is(tagToMergeInto.getId().toString()))
        .body("aliases", containsInAnyOrder("tag1"));
  }

  @Test
  @WithMockUser(roles = "admin")
  void shouldUpdateTag() {
    var tags = createTags("tag1");
    var tagToMerge = tags.get(0);
    String updatedName = "testo ergo sum";
    var aliases = Set.of("tag2", "tag3");

    RestAssuredMockMvc.given()
        .body(new UpdateTagRequest(updatedName, aliases))
        .accept("application/json")
        .contentType("application/json")
        .when()
        .put("/tags/{id}", tagToMerge.getId())
        .then()
        .statusCode(200)
        .body("id", is(tagToMerge.getId().toString()))
        .body("tagName", is("testo-ergo-sum"))
        .body("aliases", containsInAnyOrder(aliases.toArray()));
  }

  private List<Tag> createTags(String... tags) {
    var tagList = new ArrayList<Tag>();
    for (var tag : tags) {
      tagList.add(Tag.create(tag));
    }
    tagRepository.saveAll(tagList);
    return tagList;
  }
}