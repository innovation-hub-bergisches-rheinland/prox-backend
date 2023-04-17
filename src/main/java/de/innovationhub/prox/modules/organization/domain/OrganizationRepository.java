package de.innovationhub.prox.modules.organization.domain;

import java.util.Collection;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

  @Override
  @Query("select o from Organization o order by o.name asc")
  Page<Organization> findAll(Pageable pageable);

  @Query(nativeQuery = true, value = """
        WITH input AS (
            SELECT :query as query
        )
        SELECT DISTINCT o.*
        FROM input, prox_organization.organization o
        WHERE (:tagCollections IS NULL OR o.tag_collection_id IN (:tagCollections))
          AND (input.query <> '' IS NOT TRUE OR
                  word_similarity(input.query, o.name) > 0.3
              )
        ORDER BY o.name ASC
      """)
  Page<Organization> search(
      @Param("query") String query,
      @Param("tagCollections") Collection<UUID> tagIds,
      Pageable pageable);
}
