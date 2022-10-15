package de.innovationhub.prox.project.module;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ModuleType {
  private final UUID id;
  private final String key;
  private final String name;

  private final List<String> disciplines;

  private boolean active = true;

  public ModuleType(UUID id, String key, String name, List<String> disciplines) {
    this.id = id;
    this.key = key;
    this.name = name;
    this.disciplines = disciplines;
  }
}
