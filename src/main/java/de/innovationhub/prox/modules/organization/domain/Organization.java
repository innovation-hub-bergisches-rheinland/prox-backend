package de.innovationhub.prox.modules.organization.domain;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationCreated;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationLogoSet;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationMemberAdded;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationMemberRemoved;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationMemberUpdated;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationProfileUpdated;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationRenamed;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationTagCollectionUpdated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
@Table(schema = PersistenceConfig.ORGANIZATION_SCHEMA)
public class Organization extends AuditedAggregateRoot {

  @Id
  private UUID id;
  private String name;

  @Embedded
  @CollectionTable(schema = PersistenceConfig.ORGANIZATION_SCHEMA)
  private OrganizationProfile profile;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(schema = PersistenceConfig.ORGANIZATION_SCHEMA)
  private List<Membership> members = new ArrayList<>();

  private UUID tagCollectionId;

  private String logoKey;

  public Organization(UUID id, String name, List<Membership> members) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);
    Objects.requireNonNull(members);

    this.id = id;
    this.name = name;
    this.members = new ArrayList<>(members);
    this.tagCollectionId = id;
  }

  public static Organization create(String name, UUID founder) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(founder);

    var founderMembership = new Membership(founder, OrganizationRole.ADMIN);
    var createdOrganization = new Organization(UUID.randomUUID(), name, List.of(founderMembership));
    createdOrganization.registerEvent(OrganizationCreated.from(createdOrganization));
    return createdOrganization;
  }

  private boolean isMember(UUID user) {
    return getMember(user).isPresent();
  }

  private long countRoles(OrganizationRole role) {
    return members.stream()
        .filter(m -> m.getRole() == role)
        .count();
  }

  private boolean isLastAdmin(UUID user) {
    var membership = getMembership(user);

    return membership.isPresent() && membership.get().getRole() == OrganizationRole.ADMIN
        && countRoles(OrganizationRole.ADMIN) == 1;
  }

  private Optional<Membership> getMembership(UUID user) {
    return members.stream()
        .filter(m -> m.getMemberId().equals(user))
        .findFirst();
  }

  private Optional<UUID> getMember(UUID user) {
    return getMembership(user)
        .map(Membership::getMemberId);
  }

  public boolean isInRole(UUID user, OrganizationRole role) {
    return getMembership(user)
        .map(uac -> uac.getRole() == role)
        .orElse(false);
  }

  public void removeMember(UUID user) {
    if (!isMember(user)) {
      throw new RuntimeException("User is not a member of this organization");
    }

    if (isLastAdmin(user)) {
      throw new RuntimeException("Cannot remove the last admin from organization");
    }

    var member = getMembership(user)
        .orElseThrow(); // Should not happen
    members.remove(member);
    this.registerEvent(new OrganizationMemberRemoved(this.id, member.getMemberId()));
  }

  public void addMember(UUID user, OrganizationRole role) {
    if (isMember(user)) {
      throw new RuntimeException("User is already a member of this organization");
    }

    var membership = new Membership(user, role);
    members.add(membership);
    this.registerEvent(new OrganizationMemberAdded(this.id, membership));
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

  public void setTagCollectionId(UUID tagCollectionId) {
    if (tagCollectionId == null) {
      throw new IllegalArgumentException("Tag collection id cannot be null");
    }

    if (tagCollectionId.equals(this.tagCollectionId)) {
      return;
    }

    this.tagCollectionId = tagCollectionId;
    this.registerEvent(new OrganizationTagCollectionUpdated(this.id, this.tagCollectionId));
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
