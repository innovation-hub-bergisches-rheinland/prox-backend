package de.innovationhub.prox.modules.project.domain.project;

import java.time.Instant;
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
                 LEFT JOIN curriculum_context cc on p.curriculum_context_id = cc.id
                 LEFT JOIN curriculum_context_disciplines cd on cd.curriculum_context_id = cc.id
                 LEFT JOIN discipline d on d.key = cd.disciplines_key
                 LEFT JOIN curriculum_context_module_types cm on cm.curriculum_context_id = cc.id
                 LEFT JOIN module_type m on m.key = cm.module_types_key,
              to_tsvector('simple', concat_ws(' ', p.title, p.summary, p.description, p.requirement)) document,
              to_tsquery('simple', REGEXP_REPLACE(lower(:query), '\\s+', ':* & ', 'g')) query
        WHERE (:state IS NULL OR p.state = :#{#state != null ? #state.name() : ''})
            AND (:disciplineKeys IS NULL OR d.key IN (:disciplineKeys))
            AND (:moduleTypeKeys IS NULL OR m.key IN (:moduleTypeKeys))
            AND (:query <> '' IS NOT TRUE OR
                  document @@ query
              )
        ORDER BY rank DESC;
      """)
  List<Project> filterProjects(
      @Nullable @Param("state") ProjectState state,
      @Nullable @Param("disciplineKeys") Collection<String> disciplineKeys,
      @Nullable @Param("moduleTypeKeys") Collection<String> moduleTypeKeys,
      @Nullable @Param("query") String query);

  @Query("select p from Project p where p.status.state = ?1 and p.status.updatedAt <= ?2")
  List<Project> findWithStatusModifiedBefore(ProjectState status, Instant timestamp);

  @Query("select p from Project p where p.partner.organizationId = ?1")
  List<Project> findByPartner(UUID partnerId);

  @Query("select p from Project p join p.supervisors s where s.lecturerId = ?1")
  List<Project> findBySupervisor(UUID supervisorId);
}
