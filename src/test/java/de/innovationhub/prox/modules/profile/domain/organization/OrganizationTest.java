package de.innovationhub.prox.modules.profile.domain.organization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.modules.profile.domain.user.User;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationTest {

  private Organization createTestOrganization(User user) {
    return new Organization(
        UUID.randomUUID(), "ACME Ltd", List.of(new Membership(user, OrganizationRole.ADMIN)));
  }

  private Organization createTestOrganization(List<Membership> members) {
    return new Organization(UUID.randomUUID(), "ACME Ltd", members);
  }

  @Test
  void shouldAddOwnerAsAdmin() {
    var user = new User(UUID.randomUUID(), "Xavier Tester", "xavier.tester@example.com");
    var org = createTestOrganization(user);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getUser().equals(user))
        .hasSize(1)
        .first()
        .extracting(Membership::getRole)
        .isEqualTo(OrganizationRole.ADMIN);
  }

  @Test
  void shouldReturnMembersAsUnmodifiableList() {
    var user = new User(UUID.randomUUID(), "Xavier Tester", "xavier.tester@example.com");
    var org = createTestOrganization(user);

    var members = org.getMembers();
    assertThrows(UnsupportedOperationException.class, () -> members.remove(user));
  }

  @Test
  void shouldRemoveMember() {
    var owner = new User(UUID.randomUUID(), "Xavier Tester", "xavier.tester@example.com");
    var homer = new User(UUID.randomUUID(), "Homer Simpson", "homer.simpson@example.com");
    var org = createTestOrganization(owner);
    org.addMember(homer, OrganizationRole.ADMIN);

    org.removeMember(homer);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getUser().equals(homer))
        .isEmpty();
  }

  @Test
  void shouldNotRemoveLastAdmin() {
    var owner = new User(UUID.randomUUID(), "Xavier Tester", "xavier.tester@example.com");
    var org = createTestOrganization(owner);

    assertThrows(RuntimeException.class, () -> org.removeMember(owner));
  }

  @Test
  void shouldAddMember() {
    var owner = new User(UUID.randomUUID(), "Xavier Tester", "xavier.tester@example.com");
    var homer = new User(UUID.randomUUID(), "Homer Simpson", "homer.simpson@example.com");
    var org = createTestOrganization(owner);

    org.addMember(homer, OrganizationRole.ADMIN);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getUser().equals(homer))
        .hasSize(1);
  }

  @Test
  void shouldNotAddMemberTwice() {
    var owner = new User(UUID.randomUUID(), "Xavier Tester", "xavier.tester@example.com");
    var memberships = List.of(new Membership(owner, OrganizationRole.ADMIN));
    var org = createTestOrganization(memberships);

    assertThrows(RuntimeException.class, () -> org.addMember(owner, OrganizationRole.ADMIN));
  }

  @Test
  void shouldUpdateMember() {
    var owner = new User(UUID.randomUUID(), "Xavier Tester", "xavier.tester@example.com");
    var homer = new User(UUID.randomUUID(), "Homer Simpson", "homer.simpson@example.com");
    var memberships =
        List.of(
            new Membership(owner, OrganizationRole.ADMIN),
            new Membership(homer, OrganizationRole.ADMIN));
    var org = createTestOrganization(memberships);

    org.updateMembership(owner, OrganizationRole.MEMBER);

    assertThat(org.getMembers())
        .filteredOn(m -> m.getUser().equals(owner))
        .hasSize(1)
        .first()
        .extracting(Membership::getRole)
        .isEqualTo(OrganizationRole.MEMBER);
  }
}
