package de.innovationhub.prox.modules.profile.domain.lecturer;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LecturerRepository extends JpaRepository<Lecturer, UUID> {

  @Override
  @Query("select l from Lecturer l where l.visibleInPublicSearch = true order by l.name desc")
  List<Lecturer> findAll();

  @Query("select l from Lecturer l where lower(l.name) like concat('%', lower(?1), '%') and l.visibleInPublicSearch = true order by l.name desc")
  List<Lecturer> filter(String query);
}
