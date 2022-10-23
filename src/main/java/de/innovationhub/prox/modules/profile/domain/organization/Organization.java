package de.innovationhub.prox.modules.profile.domain.organization;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationCreated;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationMemberAdded;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationMemberRemoved;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationMemberUpdated;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationProfileUpdated;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationTagged;
import de.innovationhub.prox.modules.profile.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
  private UUID tagCollection;

  public static Organization create(String name, User founder) {
    var founderMembership = new Membership(founder, OrganizationRole.ADMIN);
    var createdOrganization = new Organization(UUID.randomUUID(), name, List.of(founderMembership));
    createdOrganization.registerEvent(OrganizationCreated.from(createdOrganization));
    return createdOrganization;
  }

  public Organization(UUID id, String name, List<Membership> members) {
    this.id = id;
    this.name = name;
    this.members = new ArrayList<>(members);
  }

  private boolean isMember(User userId) {
    return members.stream().anyMatch(m -> m.getUser().equals(userId));
  }

  private boolean isLastAdmin(User userId) {
    return members.stream().filter(m -> m.getRole() == OrganizationRole.ADMIN).count() == 1
        && members.stream()
        .filter(m -> m.getRole() == OrganizationRole.ADMIN && m.getUser().equals(userId)).count()
        == 1;
  }

  public void removeMember(User user) {
    if (!isMember(user)) {
      throw new RuntimeException("User is not a member of this organization");
    }

    if (isLastAdmin(user)) {
      throw new RuntimeException("Cannot remove the last admin from organization");
    }

    members.removeIf(m -> m.getUser().equals(user));
    this.registerEvent(new OrganizationMemberRemoved(this.id, user));
  }

  public void addMember(User user, OrganizationRole role) {
    if (isMember(user)) {
      throw new RuntimeException("User is already a member of this organization");
    }

    var membership = new Membership(user, role);
    members.add(membership);
    this.registerEvent(new OrganizationMemberAdded(this.id, membership));
  }

  public void updateMembership(User user, OrganizationRole role) {
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

    var membership = members.stream().filter(m -> m.getUser().equals(user)).findFirst()
        .orElseThrow(); // Should not happen
    this.members.remove(membership);
    membership.setRole(role);
    this.members.add(membership);
    this.registerEvent(new OrganizationMemberUpdated(this.id, user, membership));
  }

  public void setProfile(OrganizationProfile profile) {
    this.profile = profile;
    this.registerEvent(new OrganizationProfileUpdated(this.id, this.profile));
  }

  public List<Membership> getMembers() {
    return List.copyOf(members);
  }

  public void setTagCollection(UUID tagCollection) {
    this.tagCollection = tagCollection;
    this.registerEvent(new OrganizationTagged(this.id, this.getTagCollection()));
  }
}
