package de.innovationhub.prox.infrastructure.persistence.port;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.infrastructure.persistence.OrganizationEntities;
import de.innovationhub.prox.infrastructure.persistence.model.query.QOrganizationEntity;
import de.innovationhub.prox.profile.organization.Membership;
import de.innovationhub.prox.profile.organization.Organization;
import de.innovationhub.prox.profile.organization.OrganizationRole;
import io.ebean.DB;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EBeanOrganizationPortTest {

  EBeanOrganizationPort organizationPort = new EBeanOrganizationPort();

  @BeforeAll
  static void setup() {
    DB.saveAll(OrganizationEntities.ORGANIZATIONS);
  }

  @Test
  void save() {
    var organization = new Organization(UUID.randomUUID(), "ACME 2", Map.of(UUID.randomUUID(), new Membership(
      OrganizationRole.ADMIN)));

    var saved = organizationPort.save(organization);
    assertThat(saved)
      .isNotNull();

    var found = new QOrganizationEntity()
      .id.eq(organization.getId())
      .findOneOrEmpty();
    assertThat(found)
      .isPresent();
  }

  @Test
  void getAll() {
    assertThat(organizationPort.getAll())
      .hasSizeGreaterThanOrEqualTo(OrganizationEntities.ORGANIZATIONS.size());
  }

  @Test
  void getById() {
    assertThat(organizationPort.getById(OrganizationEntities.ACME.getId()))
      .isPresent();
  }
}
