package de.innovationhub.prox.modules.profile.domain.organization;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

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
