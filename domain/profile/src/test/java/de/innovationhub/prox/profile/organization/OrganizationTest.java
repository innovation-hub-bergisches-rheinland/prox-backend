package de.innovationhub.prox.profile.organization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationTest {
  private Organization createTestOrganization(UUID ownerId) {
    return new Organization(UUID.randomUUID(), "ACME Ltd", ownerId);
  }

  private Organization createTestOrganization(Map<UUID, Membership> members) {
    return new Organization(UUID.randomUUID(), "ACME Ltd", members);
  }

  @Test
  void shouldAddOwnerAsAdmin() {
    var userId = UUID.randomUUID();
    var org = createTestOrganization(userId);

    assertThat(org.getMembers().get(userId))
      .extracting(Membership::getRole)
      .isEqualTo(OrganizationRole.ADMIN);
  }

  @Test
  void shouldReturnMembersAsUnmodifiableMap() {
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
      .doesNotContainKey(userId);
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
      .containsKey(userId);
  }

  @Test
  void shouldNotAddMemberTwice() {
    var userId = UUID.randomUUID();
    var memberships = Map.of(userId, new Membership(OrganizationRole.ADMIN));
    var org = createTestOrganization(memberships);

    assertThrows(RuntimeException.class, () -> org.addMember(userId, OrganizationRole.ADMIN));
  }

  @Test
  void shouldUpdateMember() {
    var userId = UUID.randomUUID();
    var ownerId = UUID.randomUUID();
    var memberships = Map.of(
      ownerId, new Membership(OrganizationRole.ADMIN),
      userId, new Membership(OrganizationRole.ADMIN));
    var org = createTestOrganization(memberships);

    org.updateMembership(userId, OrganizationRole.MEMBER);

    assertThat(org.getMembers().get(userId))
      .extracting(Membership::getRole)
      .isEqualTo(OrganizationRole.MEMBER);
  }
}
