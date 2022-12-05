package de.innovationhub.prox.modules.profile.domain.lecturer;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LecturerRepository extends PagingAndSortingRepository<Lecturer, UUID> {
  @Query("select l from Lecturer l where lower(l.name) like concat('%', lower(?1), '%')")
  Page<Lecturer> filter(String query, Pageable pageable);
}
