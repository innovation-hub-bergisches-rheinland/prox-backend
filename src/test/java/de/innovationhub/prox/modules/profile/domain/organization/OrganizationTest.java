package de.innovationhub.prox.modules.profile.domain.organization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationTest {

  private Organization createTestOrganization(UUID ownerId) {
    return new Organization(
        UUID.randomUUID(), "ACME Ltd", List.of(new Membership(ownerId, OrganizationRole.ADMIN)));
  }

  private Organization createTestOrganization(List<Membership> members) {
    return new Organization(UUID.randomUUID(), "ACME Ltd", members);
  }

  @Test
  void shouldAddOwnerAsAdmin() {
    var userId = UUID.randomUUID();
    var org = createTestOrganization(userId);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getUserId().equals(userId))
        .hasSize(1)
        .first()
        .extracting(Membership::getRole)
        .isEqualTo(OrganizationRole.ADMIN);
  }

  @Test
  void shouldReturnMembersAsUnmodifiableList() {
    var userId = UUID.randomUUID();
    var org = createTestOrganization(userId);

    var members = org.getMembers();
    assertThrows(UnsupportedOperationException.class, () -> members.remove(userId));
  }

  @Test
  void shouldRemoveMember() {
    var userId = UUID.randomUUID();
    var org = createTestOrganization(UUID.randomUUID());
    org.addMember(userId, OrganizationRole.ADMIN);

    org.removeMember(userId);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getUserId().equals(userId))
        .isEmpty();
  }

  @Test
  void shouldNotRemoveLastAdmin() {
    var userId = UUID.randomUUID();
    var org = createTestOrganization(userId);

    assertThrows(RuntimeException.class, () -> org.removeMember(userId));
  }

  @Test
  void shouldAddMember() {
    var userId = UUID.randomUUID();
    var org = createTestOrganization(UUID.randomUUID());

    org.addMember(userId, OrganizationRole.ADMIN);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getUserId().equals(userId))
        .hasSize(1);
  }

  @Test
  void shouldNotAddMemberTwice() {
    var userId = UUID.randomUUID();
    var memberships = List.of(new Membership(userId, OrganizationRole.ADMIN));
    var org = createTestOrganization(memberships);

    assertThrows(RuntimeException.class, () -> org.addMember(userId, OrganizationRole.ADMIN));
  }

  @Test
  void shouldUpdateMember() {
    var userId = UUID.randomUUID();
    var ownerId = UUID.randomUUID();
    var memberships =
        List.of(
            new Membership(ownerId, OrganizationRole.ADMIN),
            new Membership(userId, OrganizationRole.ADMIN));
    var org = createTestOrganization(memberships);

    org.updateMembership(userId, OrganizationRole.MEMBER);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getUserId().equals(userId))
        .hasSize(1)
        .first()
        .extracting(Membership::getRole)
        .isEqualTo(OrganizationRole.MEMBER);
  }
}
