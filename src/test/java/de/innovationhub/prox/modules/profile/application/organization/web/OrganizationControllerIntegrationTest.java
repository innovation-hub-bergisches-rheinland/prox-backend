package de.innovationhub.prox.modules.profile.application.organization.web;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import de.innovationhub.prox.modules.profile.application.organization.dto.CreateOrganizationDto;
import de.innovationhub.prox.modules.profile.application.organization.dto.CreateOrganizationDto.CreateOrganizationProfileDto;
import de.innovationhub.prox.modules.profile.domain.organization.SocialMedia;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerIntegrationTest {
  @Autowired
  MockMvc mockMvc;

  @BeforeEach
  void setupRestAssured() {
    RestAssuredMockMvc.standaloneSetup(() -> mockMvc);
  }

  @Test
  @WithMockUser(value = "8307f5bc-38fc-44ac-bab7-3c8ef85c1ec4")
  void shouldCreateOrganization() {
    var createOrgRequest = new CreateOrganizationDto(
        "ACME Ltd.",
        new CreateOrganizationProfileDto(
            "2022-11-07",
            "200",
            "example.org",
            "test@example.org",
            "Lorem Ipsum",
            "Lala Land",
            List.of(),
            Map.of(SocialMedia.FACEBOOK, "acmeltd")
        )
    );

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(createOrgRequest)
    .when()
        .post("organizations")
    .then()
        .status(HttpStatus.CREATED);
  }
}