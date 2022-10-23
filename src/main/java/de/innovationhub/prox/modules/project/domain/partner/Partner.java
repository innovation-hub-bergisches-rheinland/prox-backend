package de.innovationhub.prox.modules.project.domain.partner;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.project.domain.partner.events.PartnerCreated;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Partner extends AbstractAggregateRoot {
  @Id
  private UUID id = UUID.randomUUID();

  @NotNull
  @Column(nullable = false)
  private String name;

  public static Partner create(String name) {
    Objects.requireNonNull(name);
    var createdPartner = new Partner(name);
    createdPartner.registerEvent(PartnerCreated.from(createdPartner));
    return createdPartner;
  }

  public Partner(String name) {
    this.name = name;
  }
}
