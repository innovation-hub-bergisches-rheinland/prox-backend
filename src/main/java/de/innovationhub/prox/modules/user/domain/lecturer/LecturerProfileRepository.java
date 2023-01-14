package de.innovationhub.prox.modules.user.domain.lecturer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LecturerProfileRepository extends JpaRepository<LecturerProfile, UUID> {

  @Query("select l from LecturerProfile l where l.userId = ?1")
  Optional<LecturerProfile> findByUserId(UUID id);

  @Override
  @Query("select l from LecturerProfile l where l.visibleInPublicSearch = true order by l.displayName asc")
  Page<LecturerProfile> findAll(Pageable pageable);

  @Query("select l from LecturerProfile l where lower(l.displayName) like concat('%', lower(?1), '%') and l.visibleInPublicSearch = true order by l.displayName asc")
  Page<LecturerProfile> filter(String query, Pageable pageable);

  @Query(nativeQuery = true, value = """
      SELECT DISTINCT l.*, up.*, ts_rank(document, q) AS rank
        FROM lecturer_profile l
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
  Page<LecturerProfile> search(
      @Param("query") String query,
      @Param("tagIds") Collection<UUID> tagIds,
      Pageable pageable);
}
