package de.innovationhub.prox.modules.organization.domain;

import de.innovationhub.prox.config.PersistenceConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(schema = PersistenceConfig.ORGANIZATION_SCHEMA)
public class Membership {
  @Id
  private UUID memberId;
  private OrganizationRole role;

  public Membership(UUID memberId, OrganizationRole role) {
    this.memberId = memberId;
    this.role = role;
  }
}
