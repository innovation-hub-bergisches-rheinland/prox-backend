package de.innovationhub.prox.modules.profile.domain.organization;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership {
  @Id
  private UUID memberId;
  private OrganizationRole role;

  public Membership(UUID memberId, OrganizationRole role) {
    this.memberId = memberId;
    this.role = role;
  }
}
