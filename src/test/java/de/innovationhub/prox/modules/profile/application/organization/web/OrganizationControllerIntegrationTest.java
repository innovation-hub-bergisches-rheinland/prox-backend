package de.innovationhub.prox.modules.profile.application.organization.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.profile.OrganizationFixtures;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.AddMembershipRequestDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.CreateOrganizationRequestDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.CreateOrganizationRequestDto.CreateOrganizationProfileDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.SetOrganizationTagsRequestDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.UpdateMembershipRequestDto;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import de.innovationhub.prox.modules.profile.domain.organization.SocialMedia;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@Transactional
@WithMockUser(value = "8307f5bc-38fc-44ac-bab7-3c8ef85c1ec4")
class OrganizationControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  OrganizationRepository organizationRepository;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @AfterEach
  void resetRestAssured() {
    organizationRepository.deleteAll();
  }

  @Test
  void shouldCreateOrganization() {
    var createOrgRequest = new CreateOrganizationRequestDto(
        "ACME Ltd.",
        new CreateOrganizationProfileDto(
            "2022-11-07",
            "200",
            "example.org",
            "test@example.org",
            "Lorem Ipsum",
            "Lala Land",
            "Lala Land",
            Map.of(SocialMedia.FACEBOOK, "acmeltd")
        )
    );

    var id = given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(createOrgRequest)
        .when()
        .post("organizations")
        .then()
        .status(HttpStatus.CREATED)
        .extract()
        .jsonPath()
        .getUUID("id");

    assertThat(organizationRepository.existsById(id)).isTrue();
  }

  @Test
  void shouldGetAll() {
    organizationRepository.saveAll(OrganizationFixtures.ALL);

    given()
        .accept(ContentType.JSON)
        .when()
        .get("organizations")
        .then()
        .status(HttpStatus.OK)
        .body(".", hasSize(OrganizationFixtures.ALL.size()));
  }

  @Test
  void shouldGetOne() {
    var org = OrganizationFixtures.ACME_LTD;
    organizationRepository.save(org);

    given()
        .accept(ContentType.JSON)
        .when()
        .get("organizations/{id}", org.getId())
        .then()
        .status(HttpStatus.OK)
        .body("id", equalTo(org.getId().toString()));
  }

  @Test
  void shouldGetMemberships() {
    var org = OrganizationFixtures.ACME_LTD;
    organizationRepository.save(org);

    given()
        .accept(ContentType.JSON)
        .when()
        .get("organizations/{id}/memberships", org.getId())
        .then()
        .status(HttpStatus.OK)
        .body("members", hasSize(org.getMembers().size()));
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldUpdateOrganization() {
    var org = OrganizationFixtures.ACME_LTD;
    organizationRepository.save(org);

    var updateOrgRequest = new CreateOrganizationRequestDto(
        "ACME Ltd.",
        new CreateOrganizationProfileDto(
            "2022-11-07",
            "200",
            "example.org",
            "test@example.org",
            "Lorem Ipsum",
            "Lala Land",
            "Lala Land",
            Map.of(SocialMedia.FACEBOOK, "acmeltd")
        )
    );

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(updateOrgRequest)
        .when()
        .put("organizations/{id}", org.getId())
        .then()
        .status(HttpStatus.OK);
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldAddMembership() {
    var org = OrganizationFixtures.ACME_LTD;
    organizationRepository.save(org);
    var userId = UUID.randomUUID();

    var addOrganizationMembershipDto = new AddMembershipRequestDto(
        userId,
        OrganizationRole.MEMBER
    );

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(addOrganizationMembershipDto)
        .when()
        .post("organizations/{id}/memberships", org.getId())
        .then()
        .status(HttpStatus.CREATED);

    var updatedOrg = organizationRepository.findById(org.getId()).get();
    assertThat(updatedOrg.isInRole(userId, OrganizationRole.MEMBER)).isTrue();
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldUpdateMembership() {
    var org = OrganizationFixtures.ACME_LTD;
    var userId = UUID.randomUUID();
    org.addMember(userId, OrganizationRole.MEMBER);
    organizationRepository.save(org);

    var updateMembershipDto = new UpdateMembershipRequestDto(
        OrganizationRole.ADMIN
    );

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(updateMembershipDto)
        .when()
        .put("organizations/{id}/memberships/{userId}", org.getId(), userId)
        .then()
        .status(HttpStatus.OK);

    var updatedOrg = organizationRepository.findById(org.getId()).get();
    assertThat(updatedOrg.isInRole(userId, OrganizationRole.ADMIN)).isTrue();
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldRemoveMembership() {
    var org = OrganizationFixtures.ACME_LTD;
    var userId = UUID.randomUUID();
    org.addMember(userId, OrganizationRole.MEMBER);
    organizationRepository.save(org);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .delete("organizations/{id}/memberships/{userId}", org.getId(), userId)
        .then()
        .status(HttpStatus.NO_CONTENT);

    var updatedOrg = organizationRepository.findById(org.getId()).get();
    assertThat(updatedOrg.getMembers())
        .filteredOn(m -> m.getMemberId().equals(userId))
        .isEmpty();
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldSetOrganizationLogo() throws IOException {
    var org = OrganizationFixtures.ACME_LTD;
    organizationRepository.save(org);

    var resource = new ClassPathResource("img/avatar.png").getFile();

    given()
        .contentType(ContentType.MULTIPART)
        .multiPart("image", resource, "image/png")
        .when()
        .post("organizations/{id}/logo", org.getId())
        .then()
        .status(HttpStatus.NO_CONTENT);

    var updatedOrg = organizationRepository.findById(org.getId()).get();
    assertThat(updatedOrg.getLogoKey()).isNotNull();
  }

  @Test
  @WithMockUser(value = "00000000-0000-0000-0000-000000000001")
  void shouldSetTags() {
    var org = OrganizationFixtures.ACME_LTD;
    organizationRepository.save(org);

    var tags = List.of(UUID.randomUUID(), UUID.randomUUID());
    var setTags = new SetOrganizationTagsRequestDto(tags);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(setTags)
        .when()
        .post("organizations/{id}/tags", org.getId().toString())
        .then()
        .status(HttpStatus.OK);

    var updatedOrg = organizationRepository.findById(org.getId()).orElseThrow();
    assertThat(updatedOrg.getTags()).containsExactlyInAnyOrderElementsOf(tags);
  }
}