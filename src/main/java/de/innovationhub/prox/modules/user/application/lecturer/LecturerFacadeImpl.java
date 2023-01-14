package de.innovationhub.prox.modules.user.application.lecturer;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.lecturer.usecase.queries.FindAllLecturerByIdsHandler;
import de.innovationhub.prox.modules.user.application.lecturer.usecase.queries.FindLecturerHandler;
import de.innovationhub.prox.modules.user.contract.lecturer.LecturerFacade;
import de.innovationhub.prox.modules.user.contract.lecturer.LecturerView;
import de.innovationhub.prox.modules.user.contract.lecturer.LecturerViewMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
public class LecturerFacadeImpl implements LecturerFacade {
  private final FindLecturerHandler findLecturerHandler;
  private final FindAllLecturerByIdsHandler findAllLecturerByIdsHandler;
  private final LecturerViewMapper lecturerViewMapper;

  @Override
  @Cacheable(CacheConfig.LECTURERS)
  public Optional<LecturerView> get(UUID id) {
    return findLecturerHandler.handle(id)
        .map(lecturerViewMapper::toView);
  }

  @Override
  @Cacheable(CacheConfig.LECTURERS)
  public List<LecturerView> findByIds(List<UUID> ids) {
    return lecturerViewMapper.toViewList(
        findAllLecturerByIdsHandler.handle(ids)
    );
  }
}
