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
      SELECT DISTINCT o.*, ts_rank(document, q) AS rank
        FROM prox_organization.organization o
                 LEFT JOIN prox_organization.organization_tags ot ON o.id = ot.organization_id,
              to_tsvector('simple', concat_ws(' ', o.name, o.contact_email)) document,
              to_tsquery('simple', REGEXP_REPLACE(lower(:query), '\\s+', ':* & ', 'g')) q
        WHERE (:tagIds IS NULL OR ot.tags IN (:tagIds))
            AND (:query <> '' IS NOT TRUE OR
                  document @@ q
              )
        ORDER BY rank DESC, o.name ASC 
      """)
  Page<Organization> search(
      @Param("query") String query,
      @Param("tagIds") Collection<UUID> tagIds,
      Pageable pageable);
}
