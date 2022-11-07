package de.innovationhub.prox.modules.project.domain.project;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, UUID> {
  @Query(nativeQuery = true, value = """
      SELECT DISTINCT p.*, ts_rank(document, query) AS rank
        FROM project p
                 LEFT JOIN (SELECT pt.project_id AS id, array_agg(pt.tags) AS tag_array
                            FROM project_tags pt
                            GROUP BY pt.project_id) tag_array USING (id)
                 LEFT JOIN project_supervisors ps on p.id = ps.project_id
                 LEFT JOIN abstract_owner ao on p.owner_id = ao.id
                 LEFT JOIN project_specializations s on p.id = s.project_id
                 LEFT JOIN specializations s2 on s.specializations_id = s2.id
                 LEFT JOIN project_modules pm on p.id = pm.project_id
                 LEFT JOIN module_type m on pm.modules_id = m.id,
              to_tsvector('simple', concat_ws(' ', p.name, p.short_description, p.requirement, ao.owner_name, ps.name,
                            array_to_string(tag_array, ' '))) document,
              to_tsquery('simple', REGEXP_REPLACE(lower(:query), '\\s+', ':* & ', 'g')) query
        WHERE (:status IS NULL OR p.status = :status)
            AND (:specializationKeys IS NULL OR s2.key IN (:specializationKeys))
            AND (:moduleTypeKeys IS NULL OR m.key IN (:moduleTypeKeys))
            AND (:query <> '' IS NOT TRUE OR
                  document @@ query
              )
        ORDER BY rank DESC, p.modified_at DESC;
    """)
  List<Project> filterProjects(
      @Nullable @Param("status") ProjectState status,
      @Nullable @Param("specializationKeys") Collection<String> specializationKeys,
      @Nullable @Param("moduleTypeKeys") Collection<String> moduleTypeKeys,
      @Nullable @Param("query") String query);
}
