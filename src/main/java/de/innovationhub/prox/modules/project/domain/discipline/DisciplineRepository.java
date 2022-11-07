package de.innovationhub.prox.modules.project.domain.discipline;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DisciplineRepository extends CrudRepository<Discipline, String> {

  @Query("select d from Discipline d where d.key in ?1")
  List<Discipline> findByKeyIn(List<String> keys);
}
