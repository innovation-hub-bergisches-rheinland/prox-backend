package de.innovationhub.prox.modules.profile.domain.lecturer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LecturerRepository extends JpaRepository<Lecturer, UUID> {

  @Query("select l from Lecturer l where l.userId = ?1")
  Optional<Lecturer> findByUserId(UUID id);

  @Override
  @Query("select l from Lecturer l where l.visibleInPublicSearch = true order by l.displayName asc")
  Page<Lecturer> findAll(Pageable pageable);

  @Query("select l from Lecturer l where lower(l.displayName) like concat('%', lower(?1), '%') and l.visibleInPublicSearch = true order by l.displayName asc")
  Page<Lecturer> filter(String query, Pageable pageable);

  @Query(nativeQuery = true, value = """
      SELECT DISTINCT l.*, up.*, ts_rank(document, q) AS rank
        FROM lecturer l
                 LEFT JOIN user_profile up ON l.id = up.id
                 LEFT JOIN user_profile_tags lt ON l.id = lt.user_profile_id,
              to_tsvector('simple', concat_ws(' ', up.display_name, l.email, l.subject)) document,
              to_tsquery('simple', REGEXP_REPLACE(lower(:query), '\\s+', ':* & ', 'g')) q
        WHERE (:tagIds IS NULL OR lt.tags IN (:tagIds))
            AND (:query <> '' IS NOT TRUE OR
                  document @@ q
              )
        ORDER BY rank DESC, up.display_name ASC 
      """)
  Page<Lecturer> search(
      @Param("query") String query,
      @Param("tagIds") Collection<UUID> tagIds,
      Pageable pageable);
}
