package de.innovationhub.prox.project.discipline;

import java.util.UUID;
import lombok.Data;

@Data
public class Discipline {
  private final UUID id;
  private final String key;
  private final String name;

  public Discipline(UUID id, String key, String name) {
    this.id = id;
    this.key = key;
    this.name = name;
  }
}
