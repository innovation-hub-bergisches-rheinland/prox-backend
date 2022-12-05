package de.innovationhub.prox.modules.project.domain.discipline;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DisciplineRepository extends PagingAndSortingRepository<Discipline, String> {

  @Query("select d from Discipline d where d.key in ?1")
  List<Discipline> findByKeyIn(List<String> keys);
}
