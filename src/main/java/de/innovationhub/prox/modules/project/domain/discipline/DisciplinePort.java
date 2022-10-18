package de.innovationhub.prox.modules.project.domain.discipline;

import java.util.List;
import java.util.Optional;

public interface DisciplinePort {
  Optional<Discipline> getByKey(String key);

  List<Discipline> getAll();
}
