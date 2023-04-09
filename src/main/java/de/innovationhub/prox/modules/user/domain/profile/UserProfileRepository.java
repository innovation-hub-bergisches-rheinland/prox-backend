package de.innovationhub.prox.modules.user.domain.profile;

import java.util.Collection;
import java.util.List;
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

  @Query("SELECT p FROM UserProfile p JOIN p.lecturerProfile lp WHERE p.visibleInPublicSearch = true")
  Page<UserProfile> findAllLecturerProfiles(Pageable pageable);

  @Query("SELECT p FROM UserProfile p JOIN p.lecturerProfile lp WHERE p.id IN (?1)")
  Page<UserProfile> findAllLecturersByIds(Collection<UUID> ids, Pageable pageable);

  @Query("SELECT p FROM UserProfile p JOIN p.lecturerProfile lp JOIN p.tags t WHERE p.visibleInPublicSearch = true AND t IN (?1)")
  Page<UserProfile> findAllLecturersWithAnyTag(List<UUID> tags, Pageable pageable);

  @Query(value = """
      WITH input AS (
          SELECT :query as query
      )
      SELECT DISTINCT up.*
      FROM input, prox_user.user_profile up
               INNER JOIN prox_user.lecturer_profile lp ON up.lecturer_profile_id = lp.id
      WHERE (input.query <> '' IS NOT TRUE OR
                word_similarity(input.query, up.display_name) > 0.5
            )
            and up.visible_in_public_search = true
      """, nativeQuery = true)
  Page<UserProfile> searchLecturers(
      @Param("query") String query,
      Pageable pageable);

  @Query("""
            SELECT DISTINCT p
              FROM UserProfile p
              WHERE (:query IS NULL OR
                        lower(p.displayName) LIKE lower(concat('%', :query, '%')) OR
                        lower(p.contactInformation.email) LIKE lower(concat('%', :query, '%')))
              AND p.visibleInPublicSearch = true
              ORDER BY p.displayName ASC
      """)
  Page<UserProfile> search(
      @Param("query") String query,
      Pageable pageable);
}
