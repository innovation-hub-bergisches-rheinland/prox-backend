package de.innovationhub.prox.modules.tag.application.tag.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.application.tag.dto.SynchronizeTagsRequest;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class TagControllerIntegrationTest extends AbstractIntegrationTest {

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

  @AfterEach
  void tearDown() {
    tagCollectionRepository.deleteAll();
    tagRepository.deleteAll();
  }

  @Test
  void shouldReturnMatchingTags() {
    var tags = createTags("tag1", "tag2", "notmatching");

    RestAssuredMockMvc.given()
        .param("q", "tag")
        .when()
        .get("/tags")
        .then()
        .statusCode(200)
        .body("id",
            containsInAnyOrder(tags.get(0).getId().toString(), tags.get(1).getId().toString()))
        .body("tagName", containsInAnyOrder(tags.get(0).getTagName(), tags.get(1).getTagName()));
  }

  @Test
  void shouldReturnRecommendation() {
    var givenTags = createTags("tag1", "tag2");
    var possiblyRecommendedTags = createTags("tag3", "tag4");

    var tagCollectionTags = new ArrayList<>(givenTags);
    tagCollectionTags.addAll(possiblyRecommendedTags);
    var tagCollection = createTagCollection(tagCollectionTags);

    RestAssuredMockMvc.given()
        .param("tags", "tag1", "tag2")
        .when()
        .get("/tags/recommendations")
        .then()
        .statusCode(200)
        .body("id", containsInAnyOrder(
            possiblyRecommendedTags.stream().map(Tag::getId).map(UUID::toString).toArray()))
        .body("tagName",
            containsInAnyOrder(possiblyRecommendedTags.stream().map(Tag::getTagName).toArray()));
  }

  @Test
  void shouldReturnRecommendationWithLimit() {
    var givenTags = createTags("tag1", "tag2");
    var possiblyRecommendedTags = createTags("tag3", "tag4");

    var tagCollectionTags = new ArrayList<>(givenTags);
    tagCollectionTags.addAll(possiblyRecommendedTags);
    var tagCollection = createTagCollection(tagCollectionTags);

    RestAssuredMockMvc.given()
        .param("tags", "tag1")
        .param("limit", 1)
        .when()
        .get("/tags/recommendations")
        .then()
        .statusCode(200)
        .body(".", hasSize(1));
  }

  @Test
  void shouldReturnPopularTags() {
    var givenTags = createTags("tag1", "tag2");

    var tagCollectionTags = new ArrayList<>(givenTags);
    var tagCollection = createTagCollection(tagCollectionTags);

    RestAssuredMockMvc.given()
        .when()
        .get("/tags/popular")
        .then()
        .statusCode(200)
        .body("id", containsInAnyOrder(
            givenTags.stream().map(Tag::getId).map(UUID::toString).toArray()))
        .body("tagName",
            containsInAnyOrder(givenTags.stream().map(Tag::getTagName).toArray()));
  }

  @Test
  @WithMockUser
  void shouldSaveNewTag() {
    var tag1 = UUID.randomUUID().toString();
    var request = new SynchronizeTagsRequest(List.of(tag1));

    var tagId = RestAssuredMockMvc.given()
        .body(request)
        .contentType("application/json")
        .accept("application/json")
        .when()
        .post("/tags/synchronization")
        .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath()
        .getUUID("tags[0].id");

    var foundTag = tagRepository.findById(tagId);
    assertThat(foundTag)
        .isPresent()
        .get()
        .extracting(Tag::getTagName)
        .isEqualTo(tag1);
  }

  @Test
  @WithMockUser
  void shouldReturnExistingTag() {
    var givenTag = createTags("tag1");
    var request = new SynchronizeTagsRequest(List.of("tag1"));

    var tagId = RestAssuredMockMvc.given()
        .body(request)
        .contentType("application/json")
        .accept("application/json")
        .when()
        .post("/tags/synchronization")
        .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath()
        .getUUID("tags[0].id");

    assertThat(tagId)
        .isEqualTo(givenTag.get(0).getId());
  }

  private List<Tag> createTags(String... tags) {
    var tagList = new ArrayList<Tag>();
    for (var tag : tags) {
      tagList.add(Tag.create(tag));
    }
    tagRepository.saveAll(tagList);
    return tagList;
  }

  private TagCollection createTagCollection(Collection<Tag> tags) {
    var tagCollection = TagCollection.create(UUID.randomUUID());
    tagCollection.setTags(tags);
    tagCollectionRepository.save(tagCollection);
    return tagCollection;
  }
}