package de.innovationhub.prox.profile.organization;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A organizational unit in PROX. This can be a company, a club/association or a laboratory. It
 * clusters lecturers to an organizational unit and allows representing the organization on PROX.
 * Also it is possible to file some entities under the tenancy of an organization.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class Organization {

  private final UUID id;
  private String name;
  private OrganizationProfile profile;
  private Map<UUID, Membership> members = new HashMap<>();
  private Set<UUID> tags = new HashSet<>();

  public Organization(UUID id, String name, UUID ownerId) {
    this.id = id;
    this.name = name;
    this.members = new HashMap<>();
    this.members.put(ownerId, new Membership(OrganizationRole.ADMIN));
  }

  public Organization(UUID id, String name, Map<UUID, Membership> members) {
    this.id = id;
    this.name = name;
    this.members = new HashMap<>(members);
  }

  public Map<UUID, Membership> getMembers() {
    return Map.copyOf(members);
  }

  public void removeMember(UUID user) {
    if (!members.containsKey(user)) {
      throw new RuntimeException("User is not a member of this organization");
    }

    var isLastAdmin =
      members.values().stream().filter(it -> it.getRole() == OrganizationRole.ADMIN).count() == 1;

    if (isLastAdmin) {
      throw new RuntimeException("Cannot remove the last admin from organization");
    }

    members.remove(user);
  }

  public void addMember(UUID user, OrganizationRole role) {
    if (members.containsKey(user)) {
      throw new RuntimeException("User is already a member of this organization");
    }

    members.put(user, new Membership(role));
  }

  public void updateMembership(UUID user, OrganizationRole role) {
    if (!members.containsKey(user)) {
      throw new RuntimeException("User is not a member of this organization");
    }

    // If we update the membership of the last admin, we are in trouble
    var isRemovingLastAdmin = role != OrganizationRole.ADMIN
      && members.values().stream().filter(it -> it.getRole() == OrganizationRole.ADMIN).count()
      == 1;

    if (isRemovingLastAdmin) {
      throw new RuntimeException("Cannot remove the last admin from organization");
    }

    members.put(user, new Membership(role));
  }

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
  }
}
