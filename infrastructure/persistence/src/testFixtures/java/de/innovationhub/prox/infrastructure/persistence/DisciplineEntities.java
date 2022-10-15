package de.innovationhub.prox.infrastructure.persistence;

import de.innovationhub.prox.infrastructure.persistence.model.DisciplineEntity;
import java.util.List;

public class DisciplineEntities {

  public static DisciplineEntity INF = new DisciplineEntity("INF", "Informatik");
  public static DisciplineEntity ING = new DisciplineEntity("ING", "Ingenieurwesen");

  public static List<DisciplineEntity> DISCIPLINES = List.of(INF, ING);
}
