package de.innovationhub.prox.infrastructure.persistence.model;

import de.innovationhub.prox.profile.organization.OrganizationRole;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationMembershipEntity extends BaseEntity {

  @EmbeddedId
  private OrganizationMembershipPK id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "organization_id", nullable = false)
  private UUID organizationId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OrganizationRole role;
}
