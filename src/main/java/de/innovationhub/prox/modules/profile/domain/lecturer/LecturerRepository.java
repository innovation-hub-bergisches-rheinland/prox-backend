package de.innovationhub.prox.modules.profile.domain.lecturer;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LecturerRepository extends CrudRepository<Lecturer, UUID> {
  @Query("select l from Lecturer l where lower(l.name) like concat('%', lower(?1), '%')")
  List<Lecturer> filter(String query);
}
