package de.innovationhub.prox.modules.user.domain.profile;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

  boolean existsByUserId(UUID userId);

  Optional<UserProfile> findByUserId(UUID uuid);

  @Query("SELECT p FROM UserProfile p JOIN p.lecturerProfile lp WHERE lp.visibleInPublicSearch = true")
  Page<UserProfile> findAllLecturerProfiles(Pageable pageable);

  //  // TODO: Tag Search not working
//  @Query(nativeQuery = true, value = """
//      SELECT DISTINCT up.*, ts_rank(document, q) AS rank
//        FROM lecturer_profile l
//                 LEFT JOIN user_profile up ON l.id = up.id
//                 LEFT JOIN lecturer_profile_tags lt ON l.id = lt.lecturer_profile_id,
//              to_tsvector('simple', concat_ws(' ', up.display_name, l.email, l.subject)) document,
//              to_tsquery('simple', REGEXP_REPLACE(lower(:query), '\\s+', ':* & ', 'g')) q
//        WHERE (:query <> '' IS NOT TRUE OR
//                  document @@ q)
//          AND l.visible_in_public_search = true
//        ORDER BY rank DESC, up.display_name ASC
//      """)
  @Query("""
            SELECT DISTINCT p
              FROM UserProfile p
                       JOIN p.lecturerProfile lp
              WHERE (:query IS NULL OR
                        lower(p.displayName) LIKE lower(concat('%', :query, '%')) OR
                        lower(lp.profile.subject) LIKE lower(concat('%', :query, '%')) OR
                        lower(lp.profile.email) LIKE lower(concat('%', :query, '%')))
                AND lp.visibleInPublicSearch = true
              ORDER BY p.displayName ASC
      """)
  Page<UserProfile> searchLecturers(
      @Param("query") String query,
      Pageable pageable);
}
