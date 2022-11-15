package de.innovationhub.prox.modules.project;

import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import java.util.List;

public class DisciplineFixtures {
  public static final Discipline INF = new Discipline(
      "INF",
      "Informatik"
  );
  public static final Discipline ING = new Discipline(
      "ING",
      "Ingenieurwesen"
  );

  public static final List<Discipline> ALL = List.of(INF, ING);
}
