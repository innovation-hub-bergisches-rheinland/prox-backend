package de.innovationhub.prox.project.discipline;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisciplinePort {
  Optional<Discipline> getById(UUID id);
  Optional<Discipline> getByKey(String key);
  List<Discipline> getAll();
}
