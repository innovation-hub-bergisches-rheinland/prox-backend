package de.innovationhub.prox.modules.profile.domain.organization;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership {

  @Id
  private UUID userId;
  private OrganizationRole role;

  public Membership(UUID userId, OrganizationRole role) {
    this.userId = userId;
    this.role = role;
  }
}
