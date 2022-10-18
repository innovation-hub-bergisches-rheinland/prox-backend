package de.innovationhub.prox.modules.project.domain.module;

import java.util.List;
import lombok.Data;

@Data
public class ModuleType {
  private final String key;
  private final String name;

  private final List<String> disciplines;

  private boolean active = true;

  public ModuleType(String key, String name, List<String> disciplines) {
    this.key = key;
    this.name = name;
    this.disciplines = disciplines;
  }
}
