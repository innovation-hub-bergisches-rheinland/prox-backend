package de.innovationhub.prox.modules.profile.domain.organization;

import de.innovationhub.prox.modules.profile.domain.user.User;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership {

  @Id
  private UUID id;

  @MapsId
  @OneToOne
  private User user;
  private OrganizationRole role;

  public Membership(User user, OrganizationRole role) {
    this.user = user;
    this.role = role;
  }
}
