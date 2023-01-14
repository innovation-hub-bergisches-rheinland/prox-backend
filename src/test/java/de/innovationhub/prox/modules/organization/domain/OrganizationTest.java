package de.innovationhub.prox.modules.organization.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.modules.organization.domain.events.OrganizationCreated;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationMemberAdded;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationMemberRemoved;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationMemberUpdated;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationTest {

  private Organization createTestOrganization(UUID user) {
    return Organization.create("ACME Ltd", user);
  }

  private Organization createTestOrganization(List<Membership> members) {
    return new Organization(UUID.randomUUID(), "ACME Ltd", members);
  }

  @Test
  void shouldAddOwnerAsAdmin() {
    var user = UUID.randomUUID();
    var org = createTestOrganization(user);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getMemberId().equals(user))
        .hasSize(1)
        .first()
        .extracting(Membership::getRole)
        .isEqualTo(OrganizationRole.ADMIN);
  }

  @Test
  void shouldReturnMembersAsUnmodifiableList() {
    var user = UUID.randomUUID();
    var org = createTestOrganization(user);

    var members = org.getMembers();
    assertThrows(UnsupportedOperationException.class, () -> members.remove(user));
  }

  @Test
  void shouldRemoveMember() {
    var owner = UUID.randomUUID();
    var homer = UUID.randomUUID();
    var org = createTestOrganization(owner);
    org.addMember(homer, OrganizationRole.ADMIN);

    org.removeMember(homer);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getMemberId().equals(homer))
        .isEmpty();
    assertThat(org.getDomainEvents())
        .filteredOn(e -> e instanceof OrganizationMemberRemoved)
        .hasSize(1);
  }

  @Test
  void shouldNotRemoveLastAdmin() {
    var owner = UUID.randomUUID();
    var org = createTestOrganization(owner);

    assertThrows(RuntimeException.class, () -> org.removeMember(owner));
  }

  @Test
  void shouldAddMember() {
    var owner = UUID.randomUUID();
    var homer = UUID.randomUUID();
    var org = createTestOrganization(owner);

    org.addMember(homer, OrganizationRole.ADMIN);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getMemberId().equals(homer))
        .hasSize(1);
    assertThat(org.getDomainEvents())
        .filteredOn(e -> e instanceof OrganizationMemberAdded)
        .hasSize(1);
  }

  @Test
  void shouldNotAddMemberTwice() {
    var owner = UUID.randomUUID();
    var memberships = List.of(new Membership(owner, OrganizationRole.ADMIN));
    var org = createTestOrganization(memberships);

    assertThrows(RuntimeException.class, () -> org.addMember(owner, OrganizationRole.ADMIN));
  }

  @Test
  void shouldUpdateMember() {
    var owner = UUID.randomUUID();
    var homer = UUID.randomUUID();
    var memberships =
        List.of(
            new Membership(owner, OrganizationRole.ADMIN),
            new Membership(homer, OrganizationRole.ADMIN));
    var org = createTestOrganization(memberships);

    org.updateMembership(owner, OrganizationRole.MEMBER);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getMemberId().equals(owner))
        .hasSize(1)
        .first()
        .extracting(Membership::getRole)
        .isEqualTo(OrganizationRole.MEMBER);

    assertThat(org.getDomainEvents())
        .filteredOn(e -> e instanceof OrganizationMemberUpdated)
        .hasSize(1);
  }

  @Test
  void shouldRegisterOrganizationCreatedEventOnCreate() {
    var user = UUID.randomUUID();
    var org = createTestOrganization(user);
    var domainEvents = org.getDomainEvents();

    assertThat(domainEvents)
        .hasSize(1)
        .first()
        .isInstanceOf(OrganizationCreated.class);
  }
}
