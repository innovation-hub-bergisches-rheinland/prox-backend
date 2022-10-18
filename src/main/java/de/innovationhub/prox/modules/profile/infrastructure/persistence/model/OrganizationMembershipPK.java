package de.innovationhub.prox.modules.profile.infrastructure.persistence.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrganizationMembershipPK {

  @Column(name = "organization_id")
  private UUID organizationId;

  @Column(name = "user_id")
  private UUID userId;
}
