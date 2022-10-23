package de.innovationhub.prox.modules.project.domain.supervisor;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.project.domain.supervisor.events.SupervisorCreated;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Supervisor extends AbstractAggregateRoot {

  @Id
  private UUID id;

  private String name;

  public static Supervisor create(UUID id, String name) {
    var createdSupervisor = new Supervisor(id, name);
    createdSupervisor.registerEvent(SupervisorCreated.from(createdSupervisor));
    return createdSupervisor;
  }

  public Supervisor(UUID id, String name) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);

    this.id = id;
    this.name = name;
  }
}
