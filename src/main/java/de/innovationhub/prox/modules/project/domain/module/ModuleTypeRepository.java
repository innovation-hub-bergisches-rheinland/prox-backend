package de.innovationhub.prox.modules.project.domain.module;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ModuleTypeRepository extends CrudRepository<ModuleType, String> {

  @Query("select m from ModuleType m where m.key in ?1")
  List<ModuleType> findByKeyIn(List<String> keys);

  @Query("select distinct m from ModuleType m join m.disciplines d where d in ?1")
  List<ModuleType> findByDisciplineKeys(Collection<String> disciplineKeys);
}
