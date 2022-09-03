package de.innovationhub.prox.project.module;

import java.util.UUID;
import lombok.Data;

@Data
public class ModuleType {
  private final UUID id;
  private final String key;
  private final String name;

  public ModuleType(UUID id, String key, String name) {
    this.id = id;
    this.key = key;
    this.name = name;
  }
}
