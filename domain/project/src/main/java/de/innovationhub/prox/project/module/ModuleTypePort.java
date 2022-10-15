package de.innovationhub.prox.project.module;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleTypePort {
  Optional<ModuleType> getById(UUID id);
  Optional<ModuleType> getByKey(String key);
  List<ModuleType> getAll();
  List<ModuleType> getByDisciplines(Collection<String> disciplineKeys);
}
