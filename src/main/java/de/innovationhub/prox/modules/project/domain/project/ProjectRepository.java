package de.innovationhub.prox.modules.project.domain.project;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

  @Query(nativeQuery = true, value = """
      SELECT DISTINCT p.*, ts_rank(p.document, q) AS rank
        FROM prox_project.project p
                 LEFT JOIN prox_project.curriculum_context cc on p.curriculum_context_id = cc.id
                 LEFT JOIN prox_project.curriculum_context_disciplines cd on cd.curriculum_context_id = cc.id
                 LEFT JOIN prox_project.curriculum_context_module_types cm on cm.curriculum_context_id = cc.id
                 LEFT JOIN prox_project.project_tags pt on pt.project_id = p.id
                 LEFT JOIN prox_project.project_supervisors ps on ps.project_id = p.id,
              to_tsquery(REGEXP_REPLACE(lower(:query), '\\s+', ':* & ', 'g')) q
        WHERE (:state IS NULL OR p.state IN (:state))
            AND (:disciplineKeys IS NULL OR cd.disciplines IN (:disciplineKeys))
            AND (:moduleTypeKeys IS NULL OR cm.module_types IN (:moduleTypeKeys))
            AND (:tagIds IS NULL OR pt.tags IN (:tagIds))
            AND (:query <> '' IS NOT TRUE OR
                  (p.document @@ q OR (:supervisorIds) IS NULL OR ps.lecturer_id IN (:supervisorIds)) 
              )
        ORDER BY rank DESC, created_at DESC
      """)
  Page<Project> filterProjects(
      @Nullable @Param("state") Collection<String> state,
      @Nullable @Param("disciplineKeys") Collection<String> disciplineKeys,
      @Nullable @Param("moduleTypeKeys") Collection<String> moduleTypeKeys,
      @Nullable @Param("query") String query,
      @Nullable @Param("tagIds") Collection<UUID> tagIds,
      @Nullable @Param("supervisorIds") Collection<UUID> supervisorIds,
      Pageable pageable);


  @Query("select p from Project p where p.status.state = ?1 and p.status.updatedAt <= ?2")
  List<Project> findWithStatusModifiedBefore(ProjectState status, Instant timestamp);

  @Query("select p from Project p where p.status.state = 'OFFERED' and p.timeBox.startDate < current_date")
  List<Project> findStartedOfferedProjects();

  @Query("select p from Project p where p.status.state = 'RUNNING' and p.timeBox.endDate < current_date")
  List<Project> findFinishedRunningProjects();

  @Query("select p from Project p where p.partner.organizationId = ?1")
  Page<Project> findByPartner(UUID partnerId, Pageable pageable);

  @Query("select p from Project p join p.supervisors s where s.lecturerId = ?1")
  Page<Project> findBySupervisor(UUID supervisorId, Pageable pageable);
}
