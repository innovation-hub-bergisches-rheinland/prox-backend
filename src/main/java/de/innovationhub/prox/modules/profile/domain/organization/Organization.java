package de.innovationhub.prox.modules.profile.domain.organization;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A organizational unit in PROX. This can be a company, a club/association or a laboratory. It
 * clusters lecturers to an organizational unit and allows representing the organization on PROX.
 * Also it is possible to file some entities under the tenancy of an organization.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Organization extends AbstractAggregateRoot {

  @Id
  private UUID id;
  private String name;

  @OneToOne
  private OrganizationProfile profile;
  @OneToMany
  private List<Membership> members = new ArrayList<>();
  @ElementCollection
  private Set<UUID> tags = new HashSet<>();

  public Organization(UUID id, String name, List<Membership> members) {
    this.id = id;
    this.name = name;
    this.members = new ArrayList<>(members);
  }

  private boolean isMember(UUID userId) {
    return members.stream().anyMatch(m -> m.getUserId().equals(userId));
  }

  private boolean isLastAdmin(UUID userId) {
    return members.stream().filter(m -> m.getRole() == OrganizationRole.ADMIN).count() == 1
        && members.stream()
        .filter(m -> m.getRole() == OrganizationRole.ADMIN && m.getUserId().equals(userId)).count()
        == 1;
  }

  public void removeMember(UUID user) {
    if (!isMember(user)) {
      throw new RuntimeException("User is not a member of this organization");
    }

    if (isLastAdmin(user)) {
      throw new RuntimeException("Cannot remove the last admin from organization");
    }

    members.removeIf(m -> m.getUserId().equals(user));
  }

  public void addMember(UUID user, OrganizationRole role) {
    if (isMember(user)) {
      throw new RuntimeException("User is already a member of this organization");
    }

    members.add(new Membership(user, role));
  }

  public void updateMembership(UUID user, OrganizationRole role) {
    if (!isMember(user)) {
      throw new RuntimeException("User is not a member of this organization");
    }

    // If we update the membership of the last admin, we are in trouble
    var isRemovingLastAdmin =
        role != OrganizationRole.ADMIN
            && isLastAdmin(user);

    if (isRemovingLastAdmin) {
      throw new RuntimeException("Cannot remove the last admin from organization");
    }

    var membership = members.stream().filter(m -> m.getUserId().equals(user)).findFirst().get();
    this.members.remove(membership);
    membership.setRole(role);
    this.members.add(membership);
  }

  public List<Membership> getMembers() {
    return List.copyOf(members);
  }

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
  }
}
