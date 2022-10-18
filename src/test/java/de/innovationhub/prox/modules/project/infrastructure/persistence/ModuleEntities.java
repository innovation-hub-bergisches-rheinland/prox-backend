package de.innovationhub.prox.modules.project.infrastructure.persistence;

import de.innovationhub.prox.modules.project.infrastructure.persistence.model.ModuleEntity;
import java.util.List;

public class ModuleEntities {

  public static ModuleEntity BA = new ModuleEntity("BA", "Bachelorarbeit", true);
  public static ModuleEntity PP = new ModuleEntity("PP", "Praxisprojekt", true);
  public static ModuleEntity TP = new ModuleEntity("TP", "Teamprojekt", true);

  public static List<ModuleEntity> MODULES = List.of(BA, PP, TP);
}
