package de.innovationhub.prox.modules.project.domain.module;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ModuleTypeRepository extends PagingAndSortingRepository<ModuleType, String> {

  @Query("select m from ModuleType m where m.key in ?1")
  List<ModuleType> findByKeyIn(List<String> keys);

  @Query("select distinct m from ModuleType m join m.disciplines d where d in ?1")
  Page<ModuleType> findByDisciplineKeys(Collection<String> disciplineKeys, Pageable pageable);
}
