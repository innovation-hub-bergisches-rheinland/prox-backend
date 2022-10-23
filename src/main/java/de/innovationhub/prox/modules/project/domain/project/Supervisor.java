package de.innovationhub.prox.modules.project.domain.project;

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
public class Supervisor {
  @Id
  private UUID id;

  private String name;

  public Supervisor(UUID id, String name) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);

    this.id = id;
    this.name = name;
  }
}
