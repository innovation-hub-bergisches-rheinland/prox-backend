package de.innovationhub.prox.modules.project.domain.module;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ModuleTypePort {

  Optional<ModuleType> getByKey(String key);

  List<ModuleType> getAll();

  List<ModuleType> getByDisciplines(Collection<String> disciplineKeys);
}
