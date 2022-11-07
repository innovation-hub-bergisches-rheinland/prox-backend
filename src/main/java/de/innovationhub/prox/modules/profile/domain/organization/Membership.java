package de.innovationhub.prox.modules.profile.domain.organization;

import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership {

  @Id
  private UUID id = UUID.randomUUID();

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
  private Member member;
  private OrganizationRole role;

  public Membership(Member member, OrganizationRole role) {
    this.member = member;
    this.role = role;
  }
}
