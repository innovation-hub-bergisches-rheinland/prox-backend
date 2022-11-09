package de.innovationhub.prox.modules.profile.domain.organization;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationCreated;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationLogoSet;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationMemberAdded;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationMemberRemoved;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationMemberUpdated;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationProfileUpdated;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationRenamed;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationTagged;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.CascadeType;
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

  @OneToOne(cascade = CascadeType.ALL)
  private OrganizationProfile profile;
  @OneToMany(cascade = CascadeType.ALL)
  private List<Membership> members = new ArrayList<>();
  private OrganizationTags tags;

  private String logoKey;

  public Organization(UUID id, String name, List<Membership> members) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);
    Objects.requireNonNull(members);

    this.id = id;
    this.name = name;
    this.members = new ArrayList<>(members);
  }

  public static Organization create(String name, UserAccount founder) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(founder);

    var founderMembership = new Membership(new Member(founder), OrganizationRole.ADMIN);
    var createdOrganization = new Organization(UUID.randomUUID(), name, List.of(founderMembership));
    createdOrganization.registerEvent(OrganizationCreated.from(createdOrganization));
    return createdOrganization;
  }

  private boolean isMember(UserAccount user) {
    return getMember(user).isPresent();
  }

  private long countRoles(OrganizationRole role) {
    return members.stream()
        .filter(m -> m.getRole() == role)
        .count();
  }

  private boolean isLastAdmin(UserAccount user) {
    var membership = getMembership(user);

    return membership.isPresent() && membership.get().getRole() == OrganizationRole.ADMIN
        && countRoles(OrganizationRole.ADMIN) == 1;
  }

  private Optional<Membership> getMembership(UserAccount user) {
    return members.stream()
        .filter(m -> m.getMember().getUser().equals(user))
        .findFirst();
  }

  private Optional<Member> getMember(UserAccount user) {
    return getMembership(user)
        .map(Membership::getMember);
  }

  public boolean isInRole(UUID user, OrganizationRole role) {
    return getMembership(new UserAccount(user))
        .map(uac -> uac.getRole() == role)
        .orElse(false);
  }

  public void removeMember(UserAccount user) {
    if (!isMember(user)) {
      throw new RuntimeException("User is not a member of this organization");
    }

    if (isLastAdmin(user)) {
      throw new RuntimeException("Cannot remove the last admin from organization");
    }

    var member = getMembership(user)
        .orElseThrow(); // Should not happen
    members.remove(member);
    this.registerEvent(new OrganizationMemberRemoved(this.id, member.getMember()));
  }

  public void addMember(UserAccount user, OrganizationRole role) {
    if (isMember(user)) {
      throw new RuntimeException("User is already a member of this organization");
    }

    var membership = new Membership(new Member(user), role);
    members.add(membership);
    this.registerEvent(new OrganizationMemberAdded(this.id, membership));
  }

  public void updateMembership(UserAccount user, OrganizationRole role) {
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

    var membership = getMembership(user)
        .orElseThrow(); // Should not happen
    this.members.remove(membership);
    membership.setRole(role);
    this.members.add(membership);
    this.registerEvent(new OrganizationMemberUpdated(this.id, membership));
  }

  public void setProfile(OrganizationProfile profile) {
    this.profile = profile;
    this.registerEvent(new OrganizationProfileUpdated(this.id, this.profile));
  }

  public List<Membership> getMembers() {
    return List.copyOf(members);
  }

  public void setTags(OrganizationTags tags) {
    this.tags = tags;
    this.registerEvent(new OrganizationTagged(this.id, this.tags.getTagCollectionId()));
  }

  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Organization title cannot be null");
    }
    if (this.name.equals(name)) {
      return;
    }

    this.name = name;
    this.registerEvent(new OrganizationRenamed(this.id, this.name));
  }

  public void setLogoKey(String logoKey) {
    this.logoKey = logoKey;
    this.registerEvent(new OrganizationLogoSet(this.id, this.logoKey));
  }
}
